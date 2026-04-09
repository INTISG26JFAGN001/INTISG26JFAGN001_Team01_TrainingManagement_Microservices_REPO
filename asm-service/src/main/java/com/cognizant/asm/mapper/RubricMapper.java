package com.cognizant.asm.mapper;

import com.cognizant.asm.entity.Rubric;
import com.cognizant.asm.dto.request.CreateRubricRequest;
import com.cognizant.asm.dto.response.RubricResponse;

import org.springframework.stereotype.Component;

@Component
public class RubricMapper {

    public Rubric toEntity(CreateRubricRequest request, Long assessmentId) {
        Rubric rubric = new Rubric();
        rubric.setAssessmentId(assessmentId);
        rubric.setCriteria(request.getCriteria());
        rubric.setWeight(request.getWeight());
        return rubric;
    }

    public RubricResponse toResponse(Rubric rubric) {
        RubricResponse r = new RubricResponse();
        r.setId(rubric.getId());
        r.setAssessmentId(rubric.getAssessmentId());
        r.setCriteria(rubric.getCriteria());
        r.setWeight(rubric.getWeight());
        return r;
    }
}