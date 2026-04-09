package com.cognizant.pes.controller;

import com.cognizant.pes.dto.ReviewRequestDTO;
import com.cognizant.pes.dto.ReviewResponseDTO;
import com.cognizant.pes.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    /**
     * US-08: Review project submissions and score[cite: 114].
     * Roles: TECH_LEAD, SCRUM_LEAD.
     */
    @PostMapping("/project/{projectId}")
//    @PreAuthorize("hasAnyRole('TECH_LEAD', 'SCRUM_LEAD')")
    public ResponseEntity<ReviewResponseDTO> submitReview(
            @PathVariable Long projectId,
            @Valid @RequestBody ReviewRequestDTO request) {

        ReviewResponseDTO response = reviewService.saveReview(projectId, request);

        // KAFKA: Topic project.events -> Publish ReviewCompleted event here

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Get a specific review by its unique ID.
     */
    @GetMapping("/{reviewId}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'TRAINER', 'TECH_LEAD', 'SCRUM_LEAD')")
    public ResponseEntity<ReviewResponseDTO> getReviewById(@PathVariable Long reviewId) {
        return ResponseEntity.ok(reviewService.getReviewById(reviewId));
    }

    /**
     * Update an existing review before final evaluation aggregation.
     */
    @PutMapping("/{reviewId}")
//    @PreAuthorize("hasAnyRole('TECH_LEAD', 'SCRUM_LEAD')")
    public ResponseEntity<ReviewResponseDTO> updateReview(
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewRequestDTO request) {

        ReviewResponseDTO response = reviewService.updateReview(reviewId, request);

        // KAFKA: Topic project.events -> Publish EvaluationUpdated event here [cite: 63]

        return ResponseEntity.ok(response);
    }

    /**
     * Delete a review record (Admin only)[cite: 66].
     */
    @DeleteMapping("/{reviewId}")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieve all reviews submitted for a specific project.
     */
    @GetMapping("/project/{projectId}/all")
//    @PreAuthorize("hasAnyRole('ADMIN', 'TRAINER', 'TECH_LEAD', 'SCRUM_LEAD')")
    public ResponseEntity<List<ReviewResponseDTO>> getReviewsByProject(@PathVariable Long projectId) {
        return ResponseEntity.ok(reviewService.getReviewsByProject(projectId));
    }

}