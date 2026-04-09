package com.cognizant.asm.integration;

import com.cognizant.asm.dto.response.InterviewResultResponse;

import org.springframework.stereotype.Component;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

// Stub implementation of InterviewResultClient.
@Component
@ConditionalOnProperty(
        name = "asm.interview-result-client.stub",
        havingValue = "true",
        matchIfMissing = true   // default to stub when property is not set
)
public class InterviewResultClientStub implements InterviewResultClient {

    @Override
    public InterviewResultResponse fetchResult(Long assessmentId, Long associateId) {
        InterviewResultResponse fallback = new InterviewResultResponse();
        fallback.setAssessmentId(assessmentId);
        fallback.setAssociateId(associateId);
        fallback.setFetchedFromExternalService(false);
        fallback.setMessage(
                "Interview result is not yet available. "
                        + "The evaluation may not have been completed, or the external evaluation service (PES) "
                        + "is currently unavailable. Please try again later."
        );
        return fallback;
    }
}