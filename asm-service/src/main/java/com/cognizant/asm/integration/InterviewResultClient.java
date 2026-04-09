package com.cognizant.asm.integration;

import com.cognizant.asm.dto.response.InterviewResultResponse;

/* Client abstraction for fetching interview evaluation results from the Project & Evaluation Service (PES).


 If the external service is unavailable or returns an error, implementations
 must return a graceful fallback InterviewResultResponse with fetchedFromExternalService = false
 and a message explaining why. */

public interface InterviewResultClient {

    InterviewResultResponse fetchResult(Long assessmentId, Long associateId);
}