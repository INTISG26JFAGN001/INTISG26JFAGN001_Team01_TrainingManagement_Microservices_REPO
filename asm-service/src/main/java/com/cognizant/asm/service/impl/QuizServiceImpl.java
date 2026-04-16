package com.cognizant.asm.service.impl;

import com.cognizant.asm.dto.request.*;
import com.cognizant.asm.entity.Quiz;
import com.cognizant.asm.entity.QuizAttemptAnswer;
import com.cognizant.asm.entity.QuizQuestion;
import com.cognizant.asm.entity.QuizAttempt;
import com.cognizant.asm.dao.QuizDAO;
import com.cognizant.asm.dao.QuizAttemptDAO;
import com.cognizant.asm.enums.AssessmentStatus;
import com.cognizant.asm.enums.ResultStatus;
import com.cognizant.asm.service.QuizService;
import com.cognizant.asm.mapper.QuizMapper;
import com.cognizant.asm.dto.response.AssessmentSummaryResponse;
import com.cognizant.asm.dto.response.QuizDetailResponse;
import com.cognizant.asm.dto.response.QuizAttemptResultResponse;
import com.cognizant.asm.exception.AssessmentNotFoundException;
import com.cognizant.asm.exception.AttemptNotFoundException;
import com.cognizant.asm.exception.AssessmentNotAvailableException;
import com.cognizant.asm.exception.DuplicateAttemptException;
import com.cognizant.asm.exception.UserNotFoundException;
import com.cognizant.asm.exception.BatchNotFoundException;

import com.cognizant.asm.integration.TesUserClient;
import com.cognizant.asm.integration.TesBatchClient;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class QuizServiceImpl implements QuizService {

    private final QuizDAO quizDAO;
    private final QuizAttemptDAO quizAttemptDAO;
    private final QuizMapper quizMapper;
    private final TesUserClient tesUserClient;
    private final TesBatchClient tesBatchClient;

    public QuizServiceImpl(QuizDAO quizDAO, QuizAttemptDAO quizAttemptDAO, QuizMapper quizMapper, TesUserClient tesUserClient, TesBatchClient tesBatchClient) {
        this.quizDAO = quizDAO;
        this.quizAttemptDAO = quizAttemptDAO;
        this.quizMapper = quizMapper;
        this.tesUserClient = tesUserClient;
        this.tesBatchClient = tesBatchClient;
    }

    @Override
    @Transactional
    public QuizDetailResponse createQuiz(CreateQuizRequest request, Long createdBy) {
        validateBatchId(request.getBatchId());
        validateTrainer(createdBy);
        Quiz quiz = quizMapper.toEntity(request);
        quiz.setCreatedBy(createdBy);
        List<QuizQuestion> questions = new ArrayList<>();
        for(QuizQuestionRequest qReq: request.getQuestions()) {
            QuizQuestion question = quizMapper.toQuestionEntity(qReq, quiz);
            questions.add(question);
        }
        quiz.setQuestions(questions);

        if(request.getMaxScore() == null || request.getMaxScore() == 0) {
            int computed = 0;
            for(QuizQuestion q : questions) {
                Integer marks = q.getMarks();
                computed += (marks == null) ? 1 : marks;
            }
            quiz.setMaxScore(computed);
        }
        Quiz saved = quizDAO.save(quiz);
        return quizMapper.toDetailResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public QuizDetailResponse getQuizById(Long quizId) {
        Quiz quiz = findQuizOrThrow(quizId);
        return quizMapper.toDetailResponse(quiz);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssessmentSummaryResponse> listQuizzesByBatch(Long batchId) {
        return quizDAO.findAll().stream()
                .filter(q -> q.getBatchId().equals(batchId))
                .map(quizMapper::toSummaryResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssessmentSummaryResponse> listQuizzesByBatchAndStatus(Long batchId, AssessmentStatus status) {
        return quizDAO.findAll().stream()
                .filter(q -> q.getBatchId().equals(batchId) && q.getStatus() == status)
                .map(quizMapper::toSummaryResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public QuizAttemptResultResponse attemptQuiz(Long quizId, QuizAttemptRequest request) {
        Quiz quiz = findQuizOrThrow(quizId);
        validateAssociate(request.getAssociateId());

        if(quiz.getStatus() != AssessmentStatus.PUBLISHED) {
            throw new AssessmentNotAvailableException(quizId);
        }

        if(quizAttemptDAO.existsByQuizIdAndAssociateId(quizId, request.getAssociateId())) {
            throw new DuplicateAttemptException(quizId, request.getAssociateId());
        }

        Map<Long, QuizQuestion> quizQuestionMap = quiz.getQuestions().stream()
                .collect(Collectors.toMap(QuizQuestion::getId, q -> q));

        int totalScore = 0;
        List<QuizAttemptAnswer> attemptAnswers = new ArrayList<>();

        for(QuizAttemptAnswerRequest ansReq : request.getAnswers()) {
            QuizQuestion question = quizQuestionMap.get(ansReq.getQuestionId());
            if(question == null) {
                continue;
            }

            boolean isCorrect = ansReq.getSelectedOption() != null && ansReq.getSelectedOption() == question.getCorrectOption();
            int marksAwarded = isCorrect ? question.getMarks() : 0;
            totalScore += marksAwarded;

            QuizAttemptAnswer answer = new QuizAttemptAnswer();
            answer.setQuestion(question);
            answer.setSelectedOption(ansReq.getSelectedOption());
            answer.setCorrect(isCorrect);
            answer.setMarksAwarded(marksAwarded);

            attemptAnswers.add(answer);
        }

        int passingMarks = quiz.getPassingMarks() != null ? quiz.getPassingMarks() : 0;
        ResultStatus resultStatus = totalScore >= passingMarks ? ResultStatus.PASS : ResultStatus.FAIL;

        QuizAttempt attempt = new QuizAttempt();
        attempt.setQuiz(quiz);
        attempt.setAssociateId(request.getAssociateId());
        attempt.setScore(totalScore);
        attempt.setResultStatus(resultStatus);
        attempt.setSubmitted(true);

        for(QuizAttemptAnswer ans : attemptAnswers) {
            ans.setQuizAttempt(attempt);
        }
        attempt.setAnswers(attemptAnswers);

        QuizAttempt saved = quizAttemptDAO.save(attempt);
        return quizMapper.toAttemptResultResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public QuizAttemptResultResponse getAttemptResult(Long quizId, Long associateId) {
        QuizAttempt attempt = quizAttemptDAO.findByQuizIdAndAssociateId(quizId, associateId)
                .orElseThrow(() -> new AttemptNotFoundException(quizId, associateId));
        return quizMapper.toAttemptResultResponse(attempt);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuizAttemptResultResponse> getAllAttempts(Long quizId) {
        findQuizOrThrow(quizId);
        return quizAttemptDAO.findByQuizId(quizId).stream()
                .map(quizMapper::toAttemptResultResponse)
                .collect(Collectors.toList());
    }

    private Quiz findQuizOrThrow(Long quizId) {
        return quizDAO.findById(quizId)
                .orElseThrow(() -> new AssessmentNotFoundException("Quiz not found with ID: " + quizId));
    }

    private void validateBatchId(Long batchId) {
        if (batchId == null) return;
        try {
            tesBatchClient.getBatchById(batchId);
        } catch (Exception ex) {
            throw new BatchNotFoundException(batchId);
        }
    }

    private void validateTrainer(Long trainerId) {
        if (trainerId == null) return;
        try {
            tesUserClient.getTrainerById(trainerId);
        } catch (Exception ex) {
            throw new UserNotFoundException("Trainer", trainerId);
        }
    }

    private void validateAssociate(Long associateId) {
        if (associateId == null) return;
        try {
            tesUserClient.getAssociateByUserId(associateId);
        } catch (Exception ex) {
            throw new UserNotFoundException("Associate", associateId);
        }
    }
}