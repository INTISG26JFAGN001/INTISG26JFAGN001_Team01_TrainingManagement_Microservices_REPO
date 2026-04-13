package com.cognizant.asm.integration;

import com.cognizant.asm.dto.response.InterviewResultResponse;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.util.List;

/*  Activated when: asm.interview-result-client.stub=false
    Falls back to InterviewResultClientStub when: asm.interview-result-client.stub=true */

@ConditionalOnProperty(
        name = "asm.interview-result-client.stub",
        havingValue = "false"
)
@FeignClient(
        name = "pes-service",
        path = "/interview-evaluations"
)
public interface PesInterviewResultClient extends InterviewResultClient {

    /* Fetches the interview evaluation result from PES.
       PES endpoint: GET /interview-evaluations/assessment/{assessmentId}/associate/{associateId}*/
    @Override
    @GetMapping("/assessment/{assessmentId}/associate/{associateId}")
    InterviewResultResponse fetchResult(
            @PathVariable("assessmentId") Long assessmentId,
            @PathVariable("associateId") Long associateId
    );

    @Override
    @GetMapping("/assessment/{assessmentId}")
    List<InterviewResultResponse> fetchAllResults(
            @PathVariable("assessmentId") Long assessmentId
    );
}