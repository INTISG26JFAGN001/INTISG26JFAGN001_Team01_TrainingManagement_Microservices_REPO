package com.cognizant.asm.controller;

import com.cognizant.asm.service.RubricService;
import com.cognizant.asm.dto.request.CreateRubricRequest;
import com.cognizant.asm.dto.response.RubricResponse;
import com.cognizant.asm.dto.response.ErrorResponseDTO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;
import jakarta.validation.Valid;

@Tag(
        name = "Rubric Management",
        description = "Manages the grading criteria, or rubrics, for interview assessments. "
                + "Rubrics define the specific skills (e.g., 'Communication' or 'Problem Solving')"
                + " that students are evaluated on and their relative weight in the total score."
)
@RestController
@RequestMapping("/assessments/{assessmentId}/rubrics")
public class RubricController {

    private RubricService rubricService;

    public RubricController(RubricService rubricService) {
        this.rubricService = rubricService;
    }

    @Operation(
            summary = "Add a grading criterion",
            description = "Adds a rubric criterion with a certain weightage to an interview assessment. " +
                    "Note that the total weight of all rubrics for a single interview must exactly equal 100 before the interview can be published."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Rubric criterion added successfully.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RubricResponse.class),
                            examples = @ExampleObject(
                                    value = """
                        {
                          "id": 1,
                          "assessmentId": 2,
                          "criteria": "Technical Knowledge",
                          "weight": 30
                        }
                        """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request: Weight exceeds 100 or fields are missing.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(
                                    value = """
                        {
                          "timeStamp": "2026-04-17T09:50:00",
                          "status": 400,
                          "error": "Bad Request",
                          "message": "Assignment of this rubric would cause the total weight to exceed 100.",
                          "path": "/assessments/50/rubrics",
                          "fieldErrors": null
                        }
                        """
                            )
                    )
            )
    })
    @PostMapping
    public ResponseEntity<RubricResponse> createRubric(@PathVariable Long assessmentId, @Valid @RequestBody CreateRubricRequest request) {
        RubricResponse response = rubricService.createRubric(assessmentId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "List all grading criteria",
            description = "Retrieves all rubrics defined for a specific interview assessment."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Rubric list retrieved successfully.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RubricResponse.class),
                            examples = @ExampleObject(
                                    value = """
                        [
                          { "id": 1, "assessmentId": 2, "criteria": "Technical Knowledge", "weight": 40 },
                          { "id": 2, "assessmentId": 2, "criteria": "Project Explanation", "weight": 30 },
                          { "id": 3, "assessmentId": 2, "criteria": "Communication & Soft Skills", "weight": 30 }
                        ]
                        """
                            )
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<RubricResponse>> getRubrics(@PathVariable Long assessmentId) {
        return ResponseEntity.ok(rubricService.getRubricsByAssessment(assessmentId));
    }

    @Operation(
            summary = "Calculate current total weight",
            description = "Computes the sum of weights for all currently defined rubric criteria for this interview. This helps trainers ensure they reach exactly 100."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Total weight retrieved successfully.",
                    content = @Content(mediaType = "application/json", schema = @Schema(type = "integer", example = "100"))
            )
    })
    @GetMapping("/total-weight")
    public ResponseEntity<Integer> getTotalWeight(@PathVariable Long assessmentId) {
        return ResponseEntity.ok(rubricService.getTotalWeight(assessmentId));
    }

    @Operation(
            summary = "Remove a grading criterion",
            description = "Permanently deletes a specific rubric criterion from the interview assessment."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Rubric criterion deleted successfully."),
            @ApiResponse(responseCode = "404", description = "No rubric found with the provided ID.")
    })
    @DeleteMapping("{rubricId}")
    public ResponseEntity<Void> deleteRubric(@PathVariable Long assessmentId, @PathVariable Long rubricId) {
        rubricService.deleteRubric(rubricId);
        return ResponseEntity.noContent().build();
    }
}