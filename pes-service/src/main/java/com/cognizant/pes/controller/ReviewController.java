package com.cognizant.pes.controller;

import com.cognizant.pes.dto.request.ReviewRequestDTO;
import com.cognizant.pes.dto.response.ReviewResponseDTO;
import com.cognizant.pes.service.impl.ReviewService;
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


    @PostMapping("/project/{projectId}")
    public ResponseEntity<ReviewResponseDTO> submitReview(
            @PathVariable Long projectId,
            @Valid @RequestBody ReviewRequestDTO request) {

        ReviewResponseDTO response = reviewService.saveReview(projectId, request);


        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDTO> getReviewById(@PathVariable Long reviewId) {
        return ResponseEntity.ok(reviewService.getReviewById(reviewId));
    }


    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDTO> updateReview(
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewRequestDTO request) {

        ReviewResponseDTO response = reviewService.updateReview(reviewId, request);


        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/project/{projectId}/all")
    public ResponseEntity<List<ReviewResponseDTO>> getReviewsByProject(@PathVariable Long projectId) {
        return ResponseEntity.ok(reviewService.getReviewsByProject(projectId));
    }

}