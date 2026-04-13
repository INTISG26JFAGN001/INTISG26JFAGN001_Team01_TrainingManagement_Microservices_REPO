package com.cognizant.asm.dto.response;

import com.cognizant.asm.enums.ResultStatus;

import lombok.Data;
import java.util.List;
import java.time.LocalDateTime;

@Data
public class QuizAttemptResultResponse {

    private Long attemptId;
    private Long quizId;
    private String quizTitle;
    private Long associateId;
    private Integer score;
    private Integer maxScore;
    private Integer passingMarks;
    private ResultStatus resultStatus;
    private boolean submitted;
    private LocalDateTime submittedAt;
    private List<QuizAttemptAnswerResponse> answers;
}