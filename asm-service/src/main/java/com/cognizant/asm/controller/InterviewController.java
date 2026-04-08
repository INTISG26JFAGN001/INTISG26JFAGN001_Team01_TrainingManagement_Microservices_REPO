package com.cognizant.asm.controller;

import com.cognizant.asm.enums.InterviewCategory;
import com.cognizant.asm.service.InterviewService;
import com.cognizant.asm.dto.request.CreateInterviewRequest;
import com.cognizant.asm.dto.request.UpdateAssessmentRequest;
import com.cognizant.asm.dto.response.AssessmentSummaryResponse;
import com.cognizant.asm.dto.response.InterviewDetailResponse;

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

    @PostMapping("/batches/{batchId}/assessments/interview")
    public ResponseEntity<InterviewDetailResponse> createInterviewForBatch(@PathVariable Long batchId, @Valid @RequestBody CreateInterviewRequest request, @RequestHeader(value = "X-User-Id", defaultValue = "0") Long createdBy) {
        request.setBatchId(batchId);
        InterviewDetailResponse response = interviewService.createInterview(request, createdBy);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{interviewId}")
    public ResponseEntity<InterviewDetailResponse> getInterviewById(@PathVariable Long interviewId, @RequestParam(required = false) Long associateId) {
        return ResponseEntity.ok(interviewService.getInterviewById(interviewId, associateId));
    }

    @GetMapping("/batch/{batchId}")
    public ResponseEntity<List<AssessmentSummaryResponse>> listByBatch(@PathVariable Long batchId) {
        return ResponseEntity.ok(interviewService.listInterviewsByBatch(batchId));
    }

    @GetMapping("/batch/{batchId}/category/{category}")
    public ResponseEntity<List<AssessmentSummaryResponse>> listByBatchAndCategory(@PathVariable Long batchId, @PathVariable InterviewCategory category) {
        return ResponseEntity.ok(interviewService.listInterviewsByBatchAndCategory(batchId, category));
    }

    @PatchMapping("/{interviewId}")
    public ResponseEntity<InterviewDetailResponse> updateInterview(@PathVariable Long interviewId, @Valid @RequestBody UpdateAssessmentRequest request) {
        return ResponseEntity.ok(interviewService.updateInterview(interviewId, request));
    }

    @DeleteMapping("/{interviewId}")
    public ResponseEntity<Void> deleteInterview(@PathVariable Long interviewId) {
        interviewService.deleteInterview(interviewId);
        return ResponseEntity.noContent().build();
    }
}