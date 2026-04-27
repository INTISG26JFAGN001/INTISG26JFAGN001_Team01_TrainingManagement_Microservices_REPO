package com.cognizant.tes.controller;

import com.cognizant.tes.client.ICourseServiceClient;
import com.cognizant.tes.dao.ITrainerTechnologyDAO;
import com.cognizant.tes.dto.ErrorResponseDTO;
import com.cognizant.tes.dto.TechnologyResponseDTO;
import com.cognizant.tes.dto.TrainerDTO;
import com.cognizant.tes.entity.Trainer;
import com.cognizant.tes.mapper.TrainerMapper;
import com.cognizant.tes.service.ITrainerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Trainer Management", description = "Endpoints for creating and managing trainers and their associated technologies")
@RestController
@RequestMapping("/trainer")
public class TrainerController {
    private final ITrainerService trainerService;
    private final ITrainerTechnologyDAO trainerTechnologyDAO;
    private final ICourseServiceClient courseServiceClient;

    public TrainerController(ITrainerService trainerService, ITrainerTechnologyDAO trainerTechnologyDAO,
                             ICourseServiceClient courseServiceClient) {
        this.trainerService = trainerService;
        this.trainerTechnologyDAO = trainerTechnologyDAO;
        this.courseServiceClient = courseServiceClient;
    }

    @Operation(summary = "Add Trainer",
            description = "Creates a new trainer with associated technologies. " +
                    "Requires a valid userId and a list of technologyIds. " +
                    "Returns the created trainer details including technology names.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Trainer created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TrainerDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Successful Response",
                                            value = "{\n" +
                                                    "  \"trainerId\": 1,\n" +
                                                    "  \"userId\": 501,\n" +
                                                    "  \"technologyIds\": [101, 102],\n" +
                                                    "  \"technologyNames\": [\"Java\", \"Spring Boot\"]\n" +
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
                                                    "  \"timestamp\": \"2026-04-16T16:48:00\",\n" +
                                                    "  \"message\": \"Failed to create trainer\",\n" +
                                                    "  \"errorCode\": \"TES-500\",\n" +
                                                    "  \"path\": \"/trainer\"\n" +
                                                    "}"
                                    )
                            }
                    )
            )
    })
    @PostMapping
    public TrainerDTO addTrainer(@Valid @RequestBody TrainerDTO trainerDTO) {
        Trainer trainer = TrainerMapper.toEntity(trainerDTO);
        Trainer savedTrainer = trainerService.addTrainer(trainer, trainerDTO.getTechnologyIds());
        List<Long> ids = getTechnologyIds(savedTrainer.getTrainerId());
        List<String> names = ids.stream()
                .map(id -> courseServiceClient.getTechnologyById(id).getName())
                .toList();
        return TrainerMapper.toDTO(savedTrainer, ids, names);
    }

    @Operation(summary = "Get Trainer by ID",
            description = "Retrieves trainer details for a specific trainer ID, including associated technologies. " +
                    "If the trainer exists, their details are returned. " +
                    "If the trainer does not exist, a 404 error is returned. " +
                    "If the provided ID is invalid (e.g., less than 1), a 400 error is returned.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Trainer retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TrainerDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Successful Response",
                                            value = "{\n" +
                                                    "  \"trainerId\": 1,\n" +
                                                    "  \"userId\": 501,\n" +
                                                    "  \"technologyIds\": [101, 102],\n" +
                                                    "  \"technologyNames\": [\"Java\", \"Spring Boot\"]\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "400",
                    description = "Invalid trainer ID provided",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Bad Request - Invalid ID",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-16T16:58:00\",\n" +
                                                    "  \"message\": \"Trainer ID must be positive\",\n" +
                                                    "  \"errorCode\": \"TES-400\",\n" +
                                                    "  \"path\": \"/trainer/0\"\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "404",
                    description = "Trainer not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Not Found Response",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-16T16:59:00\",\n" +
                                                    "  \"message\": \"Trainer with id=999 not found\",\n" +
                                                    "  \"errorCode\": \"TES-404\",\n" +
                                                    "  \"path\": \"/trainer/999\"\n" +
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
                                                    "  \"timestamp\": \"2026-04-16T17:00:00\",\n" +
                                                    "  \"message\": \"Failed to retrieve trainer\",\n" +
                                                    "  \"errorCode\": \"TES-500\",\n" +
                                                    "  \"path\": \"/trainer/1\"\n" +
                                                    "}"
                                    )
                            }
                    )
            )
    })
    @GetMapping("/{trainerId}")
    public TrainerDTO getTrainerById(@PathVariable Long trainerId) {
        Trainer trainer = trainerService.getTrainerById(trainerId);
        List<Long> ids = getTechnologyIds(trainer.getTrainerId());
        List<String> names = ids.stream()
                .map(id -> courseServiceClient.getTechnologyById(id).getName())
                .toList();
        return TrainerMapper.toDTO(trainer, ids, names);
    }

    @Operation(summary = "Delete Trainer",
            description = "Deletes a trainer by their ID. " +
                    "If the trainer exists, they are deleted and the deleted trainer details are returned. " +
                    "If the trainer does not exist, a 404 error is returned. " +
                    "If the provided ID is invalid (e.g., less than 1), a 400 error is returned.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Trainer deleted successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TrainerDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Successful Response",
                                            value = "{\n" +
                                                    "  \"trainerId\": 1,\n" +
                                                    "  \"userId\": 501,\n" +
                                                    "  \"technologyIds\": [101, 102],\n" +
                                                    "  \"technologyNames\": [\"Java\", \"Spring Boot\"]\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "400",
                    description = "Invalid trainer ID provided",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Bad Request - Invalid ID",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-16T17:10:00\",\n" +
                                                    "  \"message\": \"Trainer ID must be positive\",\n" +
                                                    "  \"errorCode\": \"TES-400\",\n" +
                                                    "  \"path\": \"/trainer/0\"\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "404",
                    description = "Trainer not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Not Found Response",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-16T17:11:00\",\n" +
                                                    "  \"message\": \"Trainer with id=999 not found\",\n" +
                                                    "  \"errorCode\": \"TES-404\",\n" +
                                                    "  \"path\": \"/trainer/999\"\n" +
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
                                                    "  \"timestamp\": \"2026-04-16T17:12:00\",\n" +
                                                    "  \"message\": \"Failed to delete trainer\",\n" +
                                                    "  \"errorCode\": \"TES-500\",\n" +
                                                    "  \"path\": \"/trainer/1\"\n" +
                                                    "}"
                                    )
                            }
                    )
            )
    })
    @DeleteMapping("/{trainerId}")
    public TrainerDTO deleteTrainer( @PathVariable Long trainerId) {
        Trainer trainer = trainerService.deleteTrainer(trainerId);
        List<Long> ids = getTechnologyIds(trainer.getTrainerId());
        List<String> names = ids.stream()
                .map(id -> courseServiceClient.getTechnologyById(id).getName())
                .toList();
        return TrainerMapper.toDTO(trainer, ids, names);
    }


    @Operation(summary = "Get All Trainers",
            description = "Retrieves all trainers in the system, including their associated technologies. " +
                    "If trainers exist, they are returned as a list of TrainerDTOs. " +
                    "If no trainers exist, the response is still 200 with an empty list.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Trainers retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TrainerDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Successful Response with Data",
                                            value = "[\n" +
                                                    "  {\n" +
                                                    "    \"trainerId\": 1,\n" +
                                                    "    \"userId\": 501,\n" +
                                                    "    \"technologyIds\": [101, 102],\n" +
                                                    "    \"technologyNames\": [\"Java\", \"Spring Boot\"]\n" +
                                                    "  },\n" +
                                                    "  {\n" +
                                                    "    \"trainerId\": 2,\n" +
                                                    "    \"userId\": 502,\n" +
                                                    "    \"technologyIds\": [103],\n" +
                                                    "    \"technologyNames\": [\"Python\"]\n" +
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
                                                    "  \"timestamp\": \"2026-04-16T17:15:00\",\n" +
                                                    "  \"message\": \"Failed to retrieve trainers\",\n" +
                                                    "  \"errorCode\": \"TES-500\",\n" +
                                                    "  \"path\": \"/trainer\"\n" +
                                                    "}"
                                    )
                            }
                    )
            )
    })
    @GetMapping
    public List<TrainerDTO> getAllTrainers() {
        return trainerService.getAllTrainers().stream()
                .map(trainer -> {
                    List<Long> ids = getTechnologyIds(trainer.getTrainerId());
                    List<String> names = ids.stream()
                            .map(id -> courseServiceClient.getTechnologyById(id).getName())
                            .toList();
                    return TrainerMapper.toDTO(trainer, ids, names);
                })
                .toList();
    }

    @Operation(summary = "Get Trainers by Technology ID",
            description = "Retrieves all trainers associated with a given technology ID, including their technology names. " +
                    "If trainers exist, they are returned as a list of TrainerDTOs. " +
                    "If no trainers exist, the response is still 200 with an empty list. " +
                    "If the technologyId is invalid (e.g., less than 1), a 400 error is returned.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Trainers retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TrainerDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Successful Response with Data",
                                            value = "[\n" +
                                                    "  {\n" +
                                                    "    \"trainerId\": 1,\n" +
                                                    "    \"userId\": 501,\n" +
                                                    "    \"technologyIds\": [101, 102],\n" +
                                                    "    \"technologyNames\": [\"Java\", \"Spring Boot\"]\n" +
                                                    "  },\n" +
                                                    "  {\n" +
                                                    "    \"trainerId\": 2,\n" +
                                                    "    \"userId\": 502,\n" +
                                                    "    \"technologyIds\": [101],\n" +
                                                    "    \"technologyNames\": [\"Java\"]\n" +
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
                    description = "Invalid technology ID provided",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Bad Request - Invalid Technology ID",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-16T17:20:00\",\n" +
                                                    "  \"message\": \"Technology ID must be positive\",\n" +
                                                    "  \"errorCode\": \"TES-400\",\n" +
                                                    "  \"path\": \"/trainer/technology?technologyId=-1\"\n" +
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
                                                    "  \"timestamp\": \"2026-04-16T17:21:00\",\n" +
                                                    "  \"message\": \"Failed to retrieve trainers by technologyId\",\n" +
                                                    "  \"errorCode\": \"TES-500\",\n" +
                                                    "  \"path\": \"/trainer/technology?technologyId=101\"\n" +
                                                    "}"
                                    )
                            }
                    )
            )
    })
    @GetMapping("/technology")
    public List<TrainerDTO> getTrainersByTechnologyId( @RequestParam Long technologyId) {
        return trainerService.getTrainersByTechnologyId(technologyId).stream()
                .map(trainer -> {
                    List<Long> ids = getTechnologyIds(trainer.getTrainerId());
                    List<String> names = ids.stream()
                            .map(id -> courseServiceClient.getTechnologyById(id).getName())
                            .toList();
                    return TrainerMapper.toDTO(trainer, ids, names);
                })
                .toList();
    }


    @Operation(summary = "Update Trainer Technologies",
            description = "Updates the list of technology IDs associated with a trainer. " +
                    "If the trainer exists, their technologies are updated and the updated trainer details are returned. " +
                    "If the trainer does not exist, a 404 error is returned. " +
                    "If the provided ID or technology list is invalid, a 400 error is returned.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Trainer technologies updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TrainerDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Successful Response",
                                            value = "{\n" +
                                                    "  \"trainerId\": 1,\n" +
                                                    "  \"userId\": 501,\n" +
                                                    "  \"technologyIds\": [101, 103],\n" +
                                                    "  \"technologyNames\": [\"Java\", \"Python\"]\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "400",
                    description = "Invalid trainer ID or technology list provided",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Bad Request - Invalid ID",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-16T17:25:00\",\n" +
                                                    "  \"message\": \"Trainer ID must be positive\",\n" +
                                                    "  \"errorCode\": \"TES-400\",\n" +
                                                    "  \"path\": \"/trainer/0/technologies\"\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "404",
                    description = "Trainer not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Not Found Response",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-16T17:27:00\",\n" +
                                                    "  \"message\": \"Trainer with id=999 not found\",\n" +
                                                    "  \"errorCode\": \"TES-404\",\n" +
                                                    "  \"path\": \"/trainer/999/technologies\"\n" +
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
                                                    "  \"timestamp\": \"2026-04-16T17:28:00\",\n" +
                                                    "  \"message\": \"Failed to update trainer technologies\",\n" +
                                                    "  \"errorCode\": \"TES-500\",\n" +
                                                    "  \"path\": \"/trainer/1/technologies\"\n" +
                                                    "}"
                                    )
                            }
                    )
            )
    })
    @PutMapping("/{trainerId}/technologies")
    public TrainerDTO updateTrainerTechnologyIds( @PathVariable Long trainerId,
                                                 @RequestBody List<Long> technologyIds) {
        Trainer updatedTrainer = trainerService.updateTrainerTechnologyIds(trainerId, technologyIds);
        List<Long> ids = getTechnologyIds(updatedTrainer.getTrainerId());
        List<String> names = ids.stream()
                .map(id -> courseServiceClient.getTechnologyById(id).getName())
                .toList();
        return TrainerMapper.toDTO(updatedTrainer, ids, names);
    }

    @Operation(summary = "Get Trainer Technologies",
            description = "Retrieves all technologies associated with a given trainer ID. " +
                    "If the trainer exists, their technologies are returned as a list of TechnologyResponseDTOs. " +
                    "If the trainer does not exist, a 404 error is returned. " +
                    "If the provided trainer ID is invalid (e.g., less than 1), a 400 error is returned.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Trainer technologies retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TechnologyResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Successful Response with Data",
                                            value = "[\n" +
                                                    "  {\n" +
                                                    "    \"technologyId\": 101,\n" +
                                                    "    \"name\": \"Java\"\n" +
                                                    "  },\n" +
                                                    "  {\n" +
                                                    "    \"technologyId\": 102,\n" +
                                                    "    \"name\": \"Spring Boot\"\n" +
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
                    description = "Invalid trainer ID provided",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Bad Request - Invalid ID",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-16T17:30:00\",\n" +
                                                    "  \"message\": \"Trainer ID must be positive\",\n" +
                                                    "  \"errorCode\": \"TES-400\",\n" +
                                                    "  \"path\": \"/trainer/0/technologies\"\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "404",
                    description = "Trainer not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Not Found Response",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-16T17:31:00\",\n" +
                                                    "  \"message\": \"Trainer with id=999 not found\",\n" +
                                                    "  \"errorCode\": \"TES-404\",\n" +
                                                    "  \"path\": \"/trainer/999/technologies\"\n" +
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
                                                    "  \"timestamp\": \"2026-04-16T17:32:00\",\n" +
                                                    "  \"message\": \"Failed to retrieve trainer technologies\",\n" +
                                                    "  \"errorCode\": \"TES-500\",\n" +
                                                    "  \"path\": \"/trainer/1/technologies\"\n" +
                                                    "}"
                                    )
                            }
                    )
            )
    })
    @GetMapping("/{trainerId}/technologies")
    public List<TechnologyResponseDTO> getTrainerTechnologies( @PathVariable Long trainerId) {
        return trainerService.getTechnologiesForTrainer(trainerId);
    }


    private List<Long> getTechnologyIds(Long trainerId) {
        return trainerTechnologyDAO.findByTrainerId(trainerId).stream()
                .map(t -> t.getTechnologyId())
                .toList();
    }


}
