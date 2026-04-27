package com.cognizant.tes.controller;

import com.cognizant.tes.dto.AssociateDTO;
import com.cognizant.tes.dto.CreateAssociateDTO;
import com.cognizant.tes.dto.ErrorResponseDTO;
import com.cognizant.tes.entity.Associate;
import com.cognizant.tes.exception.InvalidArgumentException;
import com.cognizant.tes.mapper.AssociateMapper;
import com.cognizant.tes.service.IAssociateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Tag(
        name = "Associate Management",
        description = "This controller handles operations related to associates. It provides endpoints for retrieving " +
                "associate details by user ID and may be extended to support creation, updates, and deletions."
)
@RestController
@RequestMapping("/associates")
public class AssociateController {
    private final IAssociateService associateService;;

    @Autowired
    public AssociateController(IAssociateService associateService){
        this.associateService = associateService;
    }

    @Operation(summary = "Get Associate by User ID",
            description = "Fetches the associate details for the given user ID. " +
                    "If the associate exists, it returns the associate information as a DTO. " +
                    "If the associate does not exist, an error response is returned.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Associate found successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AssociateDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Successful Response",
                                            value = "{\n" +
                                                    "  \"id\": 1,\n" +
                                                    "  \"userId\": 101,\n" +
                                                    "  \"batchId\": 2001,\n" +
                                                    "  \"xp\": 150\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "400",
                    description = "Invalid ID (negative value)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Bad Request Response",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-17T09:55:00\",\n" +
                                                    "  \"message\": \"User ID must be non-negative\",\n" +
                                                    "  \"errorCode\": \"TES-400\",\n" +
                                                    "  \"path\": \"/batches/-5\"\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "404",
                    description = "Associate not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Not Found Response",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-16T14:59:00\",\n" +
                                                    "  \"message\": \"Associate with userId 999 not found\",\n" +
                                                    "  \"errorCode\": \"A001\",\n" +
                                                    "  \"path\": \"/associates/999\"\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "403",
                    description = "Forbidden - user not authorized to create schedule",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Forbidden Response",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-16T16:40:00\",\n" +
                                                    "  \"message\": \"You are not authorized to create schedules\",\n" +
                                                    "  \"errorCode\": \"TES-403\",\n" +
                                                    "  \"path\": \"/schedule\"\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "500",
                    description = "Unexpected server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Internal Server Error",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-16T16:32:00\",\n" +
                                                    "  \"message\": \"Failed to retrieve courses for batch\",\n" +
                                                    "  \"errorCode\": \"TES-500\",\n" +
                                                    "  \"path\": \"/batches/101/courses\"\n" +
                                                    "}"
                                    )
                            }
                    )
            )
    })
    @GetMapping("/{userId}")
    public ResponseEntity<AssociateDTO> getAssociateByUserId(@PathVariable long userId){
        Associate associate = associateService.getByUserId(userId);
        AssociateDTO associateDTO = AssociateMapper.toDTO(associate);
        return ResponseEntity.ok(associateDTO);
    }

    @Operation(summary = "Get All Associates",
            description = "Retrieves a list of all associates in the system. " +
                    "Each associate is returned as a DTO containing id, userId, batchId, and xp.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "List of associates retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AssociateDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Successful Response",
                                            value = "[\n" +
                                                    "  {\n" +
                                                    "    \"id\": 1,\n" +
                                                    "    \"userId\": 101,\n" +
                                                    "    \"batchId\": 2001,\n" +
                                                    "    \"xp\": 150\n" +
                                                    "  },\n" +
                                                    "  {\n" +
                                                    "    \"id\": 2,\n" +
                                                    "    \"userId\": 102,\n" +
                                                    "    \"batchId\": 2002,\n" +
                                                    "    \"xp\": 200\n" +
                                                    "  }\n" +
                                                    "]"
                                    ),
                                    @ExampleObject(
                                            name = "Successful Response with Empty List",
                                            value = "[]"
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "403",
                    description = "Forbidden - user not authorized to create schedule",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Forbidden Response",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-16T16:40:00\",\n" +
                                                    "  \"message\": \"You are not authorized to create schedules\",\n" +
                                                    "  \"errorCode\": \"TES-403\",\n" +
                                                    "  \"path\": \"/schedule\"\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "500",
                    description = "Unexpected server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Internal Server Error",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-16T16:32:00\",\n" +
                                                    "  \"message\": \"Failed to retrieve courses for batch\",\n" +
                                                    "  \"errorCode\": \"TES-500\",\n" +
                                                    "  \"path\": \"/batches/101/courses\"\n" +
                                                    "}"
                                    )
                            }
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<AssociateDTO>> getAllAssociates(){
        List<Associate> associates = associateService.getAll();
        List<AssociateDTO> associateDTOs = associates.stream().map(AssociateMapper::toDTO).toList();
        return ResponseEntity.ok(associateDTOs);
    }

    @Operation(summary = "Get Associates by Batch ID",
            description = "Retrieves all associates belonging to the specified batch. " +
                    "If the batch ID is valid and associates exist, it returns a list of AssociateDTOs. " +
                    "If the batch ID is invalid or associates are not found, appropriate error responses are returned.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Associates retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AssociateDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Successful Response with Data",
                                            value = "[\n" +
                                                    "  {\n" +
                                                    "    \"id\": 1,\n" +
                                                    "    \"userId\": 101,\n" +
                                                    "    \"batchId\": 2001,\n" +
                                                    "    \"xp\": 150\n" +
                                                    "  },\n" +
                                                    "  {\n" +
                                                    "    \"id\": 2,\n" +
                                                    "    \"userId\": 102,\n" +
                                                    "    \"batchId\": 2001,\n" +
                                                    "    \"xp\": 200\n" +
                                                    "  }\n" +
                                                    "]"
                                    ),
                                    @ExampleObject(
                                            name = "Successful Response with Empty List",
                                            value = "[]"
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "400",
                    description = "Invalid batch ID provided",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Bad Request",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-16T15:20:00\",\n" +
                                                    "  \"message\": \"Batch ID must be a positive number\",\n" +
                                                    "  \"errorCode\": \"TES-400\",\n" +
                                                    "  \"path\": \"/associates/batch?id=-1\"\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "404",
                    description = "No associates found for the given batch ID",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Not Found Response",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-16T15:21:00\",\n" +
                                                    "  \"message\": \"No associates found for batchId 9999\",\n" +
                                                    "  \"errorCode\": \"TES-404\",\n" +
                                                    "  \"path\": \"/associates/batch?id=9999\"\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "403",
                    description = "Forbidden - user not authorized to create schedule",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Forbidden Response",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-16T16:40:00\",\n" +
                                                    "  \"message\": \"You are not authorized to create schedules\",\n" +
                                                    "  \"errorCode\": \"TES-403\",\n" +
                                                    "  \"path\": \"/schedule\"\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "500",
                    description = "Unexpected server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Internal Server Error",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-16T16:32:00\",\n" +
                                                    "  \"message\": \"Failed to retrieve courses for batch\",\n" +
                                                    "  \"errorCode\": \"TES-500\",\n" +
                                                    "  \"path\": \"/batches/101/courses\"\n" +
                                                    "}"
                                    )
                            }
                    )
            )
    })
    @GetMapping("/batch")
    public ResponseEntity<List<AssociateDTO>> getAssociatesByBatchId(@RequestParam("id") long batchId){
        List<Associate> associates = associateService.getByBatchId(batchId);
        List<AssociateDTO> associateDTOs = associates.stream().map(AssociateMapper::toDTO).toList();
        return ResponseEntity.ok(associateDTOs);
    }

    @Operation(summary = "Get Associates by XP",
            description = "Retrieves all associates whose XP matches the specified value. " +
                    "If the XP value is valid and associates exist, it returns a list of AssociateDTOs. " +
                    "If the XP value is invalid, a 400 error response is returned. " +
                    "If no associates are found, the response is still 200 with an empty list.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Associates retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AssociateDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Successful Response with Data",
                                            value = "[\n" +
                                                    "  {\n" +
                                                    "    \"id\": 1,\n" +
                                                    "    \"userId\": 101,\n" +
                                                    "    \"batchId\": 2001,\n" +
                                                    "    \"xp\": 150\n" +
                                                    "  },\n" +
                                                    "  {\n" +
                                                    "    \"id\": 2,\n" +
                                                    "    \"userId\": 102,\n" +
                                                    "    \"batchId\": 2002,\n" +
                                                    "    \"xp\": 150\n" +
                                                    "  }\n" +
                                                    "]"
                                    ),
                                    @ExampleObject(
                                            name = "Successful Response with Empty List",
                                            value = "[]"
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "400",
                    description = "Invalid XP value provided",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Bad Request",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-16T15:25:00\",\n" +
                                                    "  \"message\": \"XP value must be non-negative\",\n" +
                                                    "  \"errorCode\": \"TES-400\",\n" +
                                                    "  \"path\": \"/associates/xp?value=-10\"\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "403",
                    description = "Forbidden - user not authorized to create schedule",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Forbidden Response",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-16T16:40:00\",\n" +
                                                    "  \"message\": \"You are not authorized to create schedules\",\n" +
                                                    "  \"errorCode\": \"TES-403\",\n" +
                                                    "  \"path\": \"/schedule\"\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "500",
                    description = "Unexpected server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Internal Server Error",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-16T16:32:00\",\n" +
                                                    "  \"message\": \"Failed to retrieve courses for batch\",\n" +
                                                    "  \"errorCode\": \"TES-500\",\n" +
                                                    "  \"path\": \"/batches/101/courses\"\n" +
                                                    "}"
                                    )
                            }
                    )
            )
    })
    @GetMapping("/xp")
    public ResponseEntity<List<AssociateDTO>> getAssociatesByXp( @RequestParam("value") int xp) {

        List<Associate> associates = associateService.getByXp(xp);
        List<AssociateDTO> associateDTOs = associates.stream()
                .map(AssociateMapper::toDTO)
                .toList();
        return ResponseEntity.ok(associateDTOs);
    }


    @Operation(summary = "Create Associate",
            description = "Creates a new associate. The batchId is required, and the userId must be unique. " +
                    "If the associate is created successfully, a confirmation message is returned. " +
                    "If validation fails or constraints are violated, appropriate error responses are returned.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Associate created successfully",
                    content = @Content(mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Successful Response",
                                            value = "{\n" +
                                                    "  \"message\": \"Associate created successfully\"\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "409",
                    description = "User ID already exists",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Conflict - Duplicate User ID",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-16T15:31:00\",\n" +
                                                    "  \"message\": \"User ID already exists\",\n" +
                                                    "  \"errorCode\": \"TES-409\",\n" +
                                                    "  \"path\": \"/associates/create\"\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "403",
                    description = "Forbidden - user not authorized to create schedule",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Forbidden Response",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-16T16:40:00\",\n" +
                                                    "  \"message\": \"You are not authorized to create schedules\",\n" +
                                                    "  \"errorCode\": \"TES-403\",\n" +
                                                    "  \"path\": \"/schedule\"\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "500",
                    description = "Unexpected server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Internal Server Error",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-16T16:32:00\",\n" +
                                                    "  \"message\": \"Failed to retrieve courses for batch\",\n" +
                                                    "  \"errorCode\": \"TES-500\",\n" +
                                                    "  \"path\": \"/batches/101/courses\"\n" +
                                                    "}"
                                    )
                            }
                    )
            )
    })
    @PostMapping("/create")
    public ResponseEntity<String> createAssociate(@Valid @RequestBody CreateAssociateDTO associateDTO) {
        Associate associate = AssociateMapper.toEntity(associateDTO);
        boolean result = associateService.create(associate);
        if (result) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Associate created successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create associate");
        }
    }



    @Operation(summary = "Update Associate",
            description = "Updates an existing associate. The associate must already exist. " +
                    "If the update is successful, a confirmation message is returned. " +
                    "If the associate does not exist or the input is invalid, appropriate error responses are returned.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Associate updated successfully",
                    content = @Content(mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Successful Response",
                                            value = "{\n" +
                                                    "  \"message\": \"Associate updated successfully\"\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "404",
                    description = "Associate not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Not Found Response",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-16T15:36:00\",\n" +
                                                    "  \"message\": \"Associate with id=999 not found\",\n" +
                                                    "  \"errorCode\": \"A404\",\n" +
                                                    "  \"path\": \"/associates/update\"\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "403",
                    description = "Forbidden - user not authorized to create schedule",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Forbidden Response",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-16T16:40:00\",\n" +
                                                    "  \"message\": \"You are not authorized to create schedules\",\n" +
                                                    "  \"errorCode\": \"TES-403\",\n" +
                                                    "  \"path\": \"/schedule\"\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "500",
                    description = "Unexpected server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Internal Server Error",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-16T16:32:00\",\n" +
                                                    "  \"message\": \"Failed to retrieve courses for batch\",\n" +
                                                    "  \"errorCode\": \"TES-500\",\n" +
                                                    "  \"path\": \"/batches/101/courses\"\n" +
                                                    "}"
                                    )
                            }
                    )
            )
    })
    @PutMapping("/update")
    public ResponseEntity<String> updateAssociate(@Valid @RequestBody AssociateDTO associateDTO) {
        Associate associate = AssociateMapper.toEntity(associateDTO);
        Associate updatedAssociate = associateService.update(associate);
        return ResponseEntity.ok("Associate updated successfully");
    }

}
