package com.cognizant.asm.controller;

import com.cognizant.asm.enums.InterviewCategory;
import com.cognizant.asm.service.InterviewService;
import com.cognizant.asm.dto.request.CreateInterviewRequest;
import com.cognizant.asm.dto.response.AssessmentSummaryResponse;
import com.cognizant.asm.dto.response.InterviewDetailResponse;
import com.cognizant.asm.dto.response.InterviewResultResponse;
import com.cognizant.asm.dto.response.RubricResponse;

import jakarta.ws.rs.Path;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/assessments/interview")
public class InterviewController {

    private final InterviewService interviewService;

    public InterviewController(InterviewService interviewService) {
        this.interviewService = interviewService;
    }

    @PostMapping
    public ResponseEntity<InterviewDetailResponse> createInterview(@Valid @RequestBody CreateInterviewRequest request, @RequestHeader(value = "X-User-Id", defaultValue = "0") Long createdBy) {
        InterviewDetailResponse response = interviewService.createInterview(request, createdBy);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{interviewId}")
    public ResponseEntity<InterviewDetailResponse> getInterviewById(@PathVariable Long interviewId) {
        return ResponseEntity.ok(interviewService.getInterviewById(interviewId));
    }

    @GetMapping("/batch/{batchId}")
    public ResponseEntity<List<AssessmentSummaryResponse>> listByBatch(@PathVariable Long batchId) {
        return ResponseEntity.ok(interviewService.listInterviewsByBatch(batchId));
    }

    @GetMapping("/batch/{batchId}/category/{category}")
    public ResponseEntity<List<AssessmentSummaryResponse>> listByBatchAndCategory(@PathVariable Long batchId, @PathVariable InterviewCategory category) {
        return ResponseEntity.ok(interviewService.listInterviewsByBatchAndCategory(batchId, category));
    }

    @PostMapping("/{interviewId}/publish")
    public ResponseEntity<InterviewDetailResponse> publishInterview(@PathVariable Long interviewId) {
        return ResponseEntity.ok(interviewService.publishInterview(interviewId));
    }

    @GetMapping("/{interviewId}/results")
    public ResponseEntity<List<InterviewResultResponse>> getInterviewResults(@PathVariable Long interviewId) {
        return ResponseEntity.ok(interviewService.getInterviewResults(interviewId));
    }

    @GetMapping("/{interviewId}/associate/{associateId}")
    public ResponseEntity<InterviewResultResponse> getAssociateResult(@PathVariable Long interviewId, @PathVariable Long associateId) {
        return ResponseEntity.ok(interviewService.getAssociateResult(interviewId, associateId));
    }
}