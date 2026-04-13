package com.cognizant.asm.mapper;

import com.cognizant.asm.entity.Quiz;
import com.cognizant.asm.entity.QuizQuestion;
import com.cognizant.asm.entity.QuizAttempt;
import com.cognizant.asm.entity.QuizAttemptAnswer;
import com.cognizant.asm.enums.AssessmentType;
import com.cognizant.asm.dto.request.CreateQuizRequest;
import com.cognizant.asm.dto.request.QuizQuestionRequest;
import com.cognizant.asm.dto.response.AssessmentSummaryResponse;
import com.cognizant.asm.dto.response.QuizDetailResponse;
import com.cognizant.asm.dto.response.QuizQuestionResponse;
import com.cognizant.asm.dto.response.QuizAttemptResultResponse;
import com.cognizant.asm.dto.response.QuizAttemptAnswerResponse;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class QuizMapper {

    public Quiz toEntity(CreateQuizRequest request) {
        Quiz quiz = new Quiz();
        quiz.setTitle(request.getTitle());
        quiz.setBatchId(request.getBatchId());
        quiz.setStageId(request.getStageId());
        quiz.setDueDate(request.getDueDate());
        quiz.setMaxScore(request.getMaxScore());
        quiz.setDurationMinutes(request.getDurationMinutes());
        quiz.setPassingMarks(request.getPassingMarks());
        quiz.setStatus(request.getStatus() != null ? request.getStatus() : com.cognizant.asm.enums.AssessmentStatus.DRAFT);
        return quiz;
    }

    public QuizQuestion toQuestionEntity(QuizQuestionRequest req, Quiz quiz) {
        QuizQuestion q = new QuizQuestion();
        q.setQuiz(quiz);
        q.setQuestionText(req.getQuestionText());
        q.setOptionA(req.getOptionA());
        q.setOptionB(req.getOptionB());
        q.setOptionC(req.getOptionC());
        q.setOptionD(req.getOptionD());
        q.setCorrectOption(req.getCorrectOption());
        q.setMarks(req.getMarks() != null ? req.getMarks() : 1);
        return q;
    }

    public AssessmentSummaryResponse toSummaryResponse(Quiz quiz) {
        AssessmentSummaryResponse r = new AssessmentSummaryResponse();
        r.setId(quiz.getId());
        r.setTitle(quiz.getTitle());
        r.setType(AssessmentType.QUIZ);
        r.setStatus(quiz.getStatus());
        r.setBatchId(quiz.getBatchId());
        r.setStageId(quiz.getStageId());
        r.setDueDate(quiz.getDueDate());
        r.setMaxScore(quiz.getMaxScore());
        r.setCreatedBy(quiz.getCreatedBy());
        return r;
    }

    public QuizDetailResponse toDetailResponse(Quiz quiz) {
        QuizDetailResponse response = new QuizDetailResponse();
        response.setId(quiz.getId());
        response.setTitle(quiz.getTitle());
        response.setType(AssessmentType.QUIZ);
        response.setStatus(quiz.getStatus());
        response.setBatchId(quiz.getBatchId());
        response.setStageId(quiz.getStageId());
        response.setDueDate(quiz.getDueDate());
        response.setMaxScore(quiz.getMaxScore());
        response.setDurationMinutes(quiz.getDurationMinutes());
        response.setPassingMarks(quiz.getPassingMarks());
        response.setCreatedBy(quiz.getCreatedBy());
        List<QuizQuestionResponse> questionResponses = quiz.getQuestions().stream()
                .map(this::toQuestionResponse)
                .collect(Collectors.toList());
        response.setQuestions(questionResponses);
        return response;
    }

    public QuizQuestionResponse toQuestionResponse(QuizQuestion q) {
        QuizQuestionResponse r = new QuizQuestionResponse();
        r.setId(q.getId());
        r.setQuestionText(q.getQuestionText());
        r.setOptionA(q.getOptionA());
        r.setOptionB(q.getOptionB());
        r.setOptionC(q.getOptionC());
        r.setOptionD(q.getOptionD());
        r.setMarks(q.getMarks());
        return r;
    }

    public QuizAttemptResultResponse toAttemptResultResponse(QuizAttempt attempt) {
        QuizAttemptResultResponse r = new QuizAttemptResultResponse();
        r.setAttemptId(attempt.getId());
        r.setQuizId(attempt.getQuiz().getId());
        r.setQuizTitle(attempt.getQuiz().getTitle());
        r.setAssociateId(attempt.getAssociateId());
        r.setScore(attempt.getScore());
        r.setMaxScore(attempt.getQuiz().getMaxScore());
        r.setPassingMarks(attempt.getQuiz().getPassingMarks());
        r.setResultStatus(attempt.getResultStatus());
        r.setSubmitted(attempt.isSubmitted());
        r.setSubmittedAt(attempt.getSubmittedAt());
        List<QuizAttemptAnswerResponse> answerResponses = attempt.getAnswers().stream()
                .map(this::toAnswerResponse)
                .collect(Collectors.toList());
        r.setAnswers(answerResponses);
        return r;
    }

    public QuizAttemptAnswerResponse toAnswerResponse(QuizAttemptAnswer ans) {
        QuizAttemptAnswerResponse r = new QuizAttemptAnswerResponse();
        r.setQuestionId(ans.getQuestion().getId());
        r.setQuestionText(ans.getQuestion().getQuestionText());
        r.setSelectedOption(ans.getSelectedOption());
        r.setCorrectOption(ans.getQuestion().getCorrectOption());
        r.setCorrect(ans.isCorrect());
        r.setMarksAwarded(ans.getMarksAwarded());
        return r;
    }
}