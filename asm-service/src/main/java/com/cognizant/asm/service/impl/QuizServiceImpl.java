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

    public QuizServiceImpl(QuizDAO quizDAO, QuizAttemptDAO quizAttemptDAO, QuizMapper quizMapper) {
        this.quizDAO = quizDAO;
        this.quizAttemptDAO = quizAttemptDAO;
        this.quizMapper = quizMapper;
    }

    @Override
    @Transactional
    public QuizDetailResponse createQuiz(CreateQuizRequest request, Long createdBy) {
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
    public QuizDetailResponse updateQuiz(Long quizId, UpdateAssessmentRequest request) {
        Quiz quiz = findQuizOrThrow(quizId);
        if(request.getTitle() != null) {
            quiz.setTitle(request.getTitle());
        }
        if(request.getDueDate() != null){
            quiz.setDueDate(request.getDueDate());
        }
        if(request.getMaxScore() != null){
            quiz.setMaxScore(request.getMaxScore());
        }
        if(request.getStatus() != null){
            quiz.setStatus(request.getStatus());
        }
        Quiz saved = quizDAO.save(quiz);
        return quizMapper.toDetailResponse(saved);
    }

    @Override
    @Transactional
    public void deleteQuiz(Long quizId) {
        Quiz quiz = findQuizOrThrow(quizId);
        quizDAO.deleteById(quizId);
    }

    @Override
    @Transactional
    public QuizAttemptResultResponse attemptQuiz(Long quizId, QuizAttemptRequest request) {
        Quiz quiz = findQuizOrThrow(quizId);

        if(quiz.getStatus() != AssessmentStatus.PUBLISHED) {
            throw new AssessmentNotAvailableException(quizId);
        }

        if(quizAttemptDAO.existsByAssessmentIdAndAssociateId(quizId, request.getAssociateId())) {
            throw new DuplicateAttemptException(quizId, request.getAssociateId());
        }

        Map<Long, QuizQuestion> quizQuestionMap = quiz.getQuestions().stream()
                .collect(Collectors.toMap(q -> q.getId(), q -> q));

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
        QuizAttempt attempt = quizAttemptDAO.findByAssessmentIdAndAssociateId(quizId, associateId)
                .orElseThrow(() -> new AttemptNotFoundException(quizId, associateId));
        return quizMapper.toAttemptResultResponse(attempt);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuizAttemptResultResponse> getAllAttempts(Long quizId) {
        findQuizOrThrow(quizId);
        return quizAttemptDAO.findByAssessmentId(quizId).stream()
                .map(quizMapper::toAttemptResultResponse)
                .collect(Collectors.toList());
    }

    private Quiz findQuizOrThrow(Long quizId) {
        return quizDAO.findById(quizId)
                .orElseThrow(() -> new AssessmentNotFoundException("Quiz not found with ID: " + quizId));
    }
}