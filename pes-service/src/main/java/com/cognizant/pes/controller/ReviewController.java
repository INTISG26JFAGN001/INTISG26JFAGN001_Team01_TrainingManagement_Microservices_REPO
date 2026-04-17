package com.cognizant.pes.controller;

import com.cognizant.pes.dto.request.ReviewRequestDTO;
import com.cognizant.pes.dto.response.ReviewResponseDTO;
import com.cognizant.pes.service.impl.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@Tag(
        name = "Review Controller",
        description = "This controller manages project reviews submitted by evaluators such as technical or Scrum leads. " +
                "It provides APIs to submit, retrieve, update, and delete reviews, as well as to fetch all reviews " +
                "associated with a specific project."
)
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Operation(
            summary = "Submit Project Review",
            description = "Submits a review for a given project. The review can be of type TECH or SCRUM and includes " +
                    "a score and evaluator comments. Each review is linked to a specific project."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Review submitted successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReviewResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Review Created",
                                    value = "{\n" +
                                            "  \"projectId\": 201,\n" +
                                            "  \"reviewerId\": 9001,\n" +
                                            "  \"score\": 85,\n" +
                                            "  \"comments\": \"Well-structured code and clean architecture\",\n" +
                                            "  \"type\": \"TECH\"\n" +
                                            "}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid review input",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\n" +
                                            "  \"timestamp\": \"2026-04-17T10:40:00\",\n" +
                                            "  \"message\": \"Score cannot exceed 100\",\n" +
                                            "  \"errorCode\": \"R001\",\n" +
                                            "  \"path\": \"/reviews/project/201\"\n" +
                                            "}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Project not found"
            )
    })
    @PostMapping("/project/{projectId}")
    public ResponseEntity<ReviewResponseDTO> submitReview(
            @PathVariable Long projectId,
            @Valid @RequestBody ReviewRequestDTO request) {

        ReviewResponseDTO response =
                reviewService.saveReview(projectId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Get Review by ID",
            description = "Retrieves a single review using its unique review identifier."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Review retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReviewResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Review not found"
            )
    })
    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDTO> getReviewById(
            @PathVariable Long reviewId) {

        return ResponseEntity.ok(reviewService.getReviewById(reviewId));
    }

    @Operation(
            summary = "Update Review",
            description = "Updates an existing project review. The review ID must be valid."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Review updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReviewResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Review not found"
            )
    })
    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDTO> updateReview(
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewRequestDTO request) {

        ReviewResponseDTO response =
                reviewService.updateReview(reviewId, request);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Delete Review",
            description = "Deletes an existing project review using its review ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Review deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Review not found")
    })
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable Long reviewId) {

        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get All Reviews for a Project",
            description = "Fetches all reviews submitted for a given project ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Project reviews retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReviewResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Project not found"
            )
    })
    @GetMapping("/project/{projectId}/all")
    public ResponseEntity<List<ReviewResponseDTO>> getReviewsByProject(
            @PathVariable Long projectId) {

        return ResponseEntity.ok(
                reviewService.getReviewsByProject(projectId)
        );
    }
}