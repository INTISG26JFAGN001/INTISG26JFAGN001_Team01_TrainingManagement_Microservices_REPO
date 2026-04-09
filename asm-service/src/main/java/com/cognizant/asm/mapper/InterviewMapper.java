package com.cognizant.asm.mapper;

import com.cognizant.asm.entity.Interview;
import com.cognizant.asm.entity.Rubric;
import com.cognizant.asm.enums.AssessmentStatus;
import com.cognizant.asm.enums.AssessmentType;
import com.cognizant.asm.dto.request.CreateInterviewRequest;
import com.cognizant.asm.dto.response.AssessmentSummaryResponse;
import com.cognizant.asm.dto.response.InterviewDetailResponse;
import com.cognizant.asm.dto.response.InterviewResultResponse;
import com.cognizant.asm.dto.response.RubricResponse;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class InterviewMapper {

    public Interview toEntity(CreateInterviewRequest request) {
        Interview interview = new Interview();
        interview.setTitle(request.getTitle());
        interview.setBatchId(request.getBatchId());
        interview.setStageId(request.getStageId());
        interview.setDueDate(request.getDueDate());
        interview.setMaxScore(request.getMaxScore() != null ? request.getMaxScore() : 100);
        interview.setInterviewCategory(request.getInterviewCategory());
        interview.setTotalMarks(request.getTotalMarks() != null ? request.getTotalMarks() : 100);
        interview.setScheduledDateTime(request.getScheduledDateTime());
        interview.setEvaluatorRole(request.getEvaluatorRole());
        interview.setStatus(request.getStatus() != null ? request.getStatus() : AssessmentStatus.DRAFT);
        return interview;
    }

    public InterviewDetailResponse toDetailResponse(Interview interview, List<Rubric> rubrics, InterviewResultResponse externalResult) {
        InterviewDetailResponse response = new InterviewDetailResponse();
        response.setId(interview.getId());
        response.setTitle(interview.getTitle());
        response.setType(AssessmentType.INTERVIEW);
        response.setStatus(interview.getStatus());
        response.setBatchId(interview.getBatchId());
        response.setStageId(interview.getStageId());
        response.setDueDate(interview.getDueDate());
        response.setMaxScore(interview.getMaxScore());
        response.setInterviewCategory(interview.getInterviewCategory());
        response.setTotalMarks(interview.getTotalMarks());
        response.setScheduledDateTime(interview.getScheduledDateTime());
        response.setEvaluatorRole(interview.getEvaluatorRole());
        response.setCreatedBy(interview.getCreatedBy());

        List<RubricResponse> rubricResponses = rubrics.stream()
                .map(this::toRubricResponse)
                .collect(Collectors.toList());
        response.setRubrics(rubricResponses);
        // External result — may be null (graceful fallback handled in service / client)
        response.setInterviewResult(externalResult);
        return response;
    }

    public AssessmentSummaryResponse toSummaryResponse(Interview interview) {
        AssessmentSummaryResponse r = new AssessmentSummaryResponse();
        r.setId(interview.getId());
        r.setTitle(interview.getTitle());
        r.setType(AssessmentType.INTERVIEW);
        r.setStatus(interview.getStatus());
        r.setBatchId(interview.getBatchId());
        r.setStageId(interview.getStageId());
        r.setDueDate(interview.getDueDate());
        r.setMaxScore(interview.getMaxScore());
        r.setCreatedBy(interview.getCreatedBy());
        return r;
    }

    public RubricResponse toRubricResponse(Rubric rubric) {
        RubricResponse r = new RubricResponse();
        r.setId(rubric.getId());
        r.setAssessmentId(rubric.getAssessmentId());
        r.setCriteria(rubric.getCriteria());
        r.setWeight(rubric.getWeight());
        return r;
    }
}