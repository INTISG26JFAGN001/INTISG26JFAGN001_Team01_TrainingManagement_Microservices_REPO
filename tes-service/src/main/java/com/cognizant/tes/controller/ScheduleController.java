package com.cognizant.tes.controller;

import com.cognizant.tes.dto.ErrorResponseDTO;
import com.cognizant.tes.dto.ScheduleDTO;
import com.cognizant.tes.entity.Schedule;
import com.cognizant.tes.mapper.ScheduleMapper;
import com.cognizant.tes.service.IScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Schedule Management", description = "Endpoints for creating and managing schedules for batches")
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    private final IScheduleService scheduleService;

    public ScheduleController(IScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @Operation(summary = "Add Schedule",
            description = "Creates a new schedule for a batch. Requires a valid batchId and sessionDate. " +
                    "If successful, the created schedule details are returned.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Schedule created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ScheduleDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Successful Response",
                                            value = "{\n" +
                                                    "  \"scheduleId\": 1,\n" +
                                                    "  \"batchId\": 101,\n" +
                                                    "  \"sessionDate\": \"2026-04-16T09:00:00\"\n" +
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
                                                    "  \"timestamp\": \"2026-04-16T16:36:00\",\n" +
                                                    "  \"message\": \"Failed to create schedule\",\n" +
                                                    "  \"errorCode\": \"TES-500\",\n" +
                                                    "  \"path\": \"/schedule\"\n" +
                                                    "}"
                                    )
                            }
                    )
            )
    })
    @PostMapping
    public ScheduleDTO addSchedule(@Valid @RequestBody ScheduleDTO scheduleDTO) {
        Schedule schedule = scheduleService.addSchedule(scheduleDTO);
        return ScheduleMapper.toDTO(schedule);
    }

    @Operation(summary = "Get Schedule by ID",
            description = "Retrieves schedule details for a specific schedule ID. " +
                    "If the schedule exists, its details are returned. " +
                    "If the schedule does not exist, a 404 error is returned. " +
                    "If the provided ID is invalid (e.g., less than 1), a 400 error is returned.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Schedule retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ScheduleDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Successful Response",
                                            value = "{\n" +
                                                    "  \"scheduleId\": 1,\n" +
                                                    "  \"batchId\": 101,\n" +
                                                    "  \"sessionDate\": \"2026-04-16T09:00:00\"\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "400",
                    description = "Invalid schedule ID provided",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Bad Request - Invalid ID",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-16T16:40:00\",\n" +
                                                    "  \"message\": \"Schedule ID must be positive\",\n" +
                                                    "  \"errorCode\": \"TES-400\",\n" +
                                                    "  \"path\": \"/schedule/0\"\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "404",
                    description = "Schedule not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Not Found Response",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-16T16:41:00\",\n" +
                                                    "  \"message\": \"Schedule with id=999 not found\",\n" +
                                                    "  \"errorCode\": \"TES-404\",\n" +
                                                    "  \"path\": \"/schedule/999\"\n" +
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
                                                    "  \"timestamp\": \"2026-04-16T16:42:00\",\n" +
                                                    "  \"message\": \"Failed to retrieve schedule\",\n" +
                                                    "  \"errorCode\": \"TES-500\",\n" +
                                                    "  \"path\": \"/schedule/1\"\n" +
                                                    "}"
                                    )
                            }
                    )
            )
    })
    @GetMapping("/{scheduleId}")
    public ScheduleDTO getScheduleById(@PathVariable("scheduleId") Long scheduleId) {
        Schedule schedule = scheduleService.getScheduleById(scheduleId);
        return ScheduleMapper.toDTO(schedule);
    }

    @Operation(summary = "Get All Schedules",
            description = "Retrieves all schedules in the system. " +
                    "If schedules exist, they are returned as a list of ScheduleDTOs. " +
                    "If no schedules exist, the response is still 200 with an empty list.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Schedules retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ScheduleDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Successful Response with Data",
                                            value = "[\n" +
                                                    "  {\n" +
                                                    "    \"scheduleId\": 1,\n" +
                                                    "    \"batchId\": 101,\n" +
                                                    "    \"sessionDate\": \"2026-04-16T09:00:00\"\n" +
                                                    "  },\n" +
                                                    "  {\n" +
                                                    "    \"scheduleId\": 2,\n" +
                                                    "    \"batchId\": 102,\n" +
                                                    "    \"sessionDate\": \"2026-04-17T14:00:00\"\n" +
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
                                                    "  \"timestamp\": \"2026-04-16T16:45:00\",\n" +
                                                    "  \"message\": \"Failed to retrieve schedules\",\n" +
                                                    "  \"errorCode\": \"TES-500\",\n" +
                                                    "  \"path\": \"/schedule\"\n" +
                                                    "}"
                                    )
                            }
                    )
            )
    })
    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        List<Schedule> schedules = scheduleService.getAllSchedules();
        return schedules.stream().map(ScheduleMapper::toDTO).toList();
    }

    @Operation(summary = "Update Session Date",
            description = "Updates the session date for a schedule by its ID. " +
                    "If the schedule exists, its session date is updated and the updated schedule details are returned. " +
                    "If the schedule does not exist, a 404 error is returned. " +
                    "If the provided ID or session date is invalid, a 400 error is returned.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Session date updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ScheduleDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Successful Response",
                                            value = "{\n" +
                                                    "  \"scheduleId\": 1,\n" +
                                                    "  \"batchId\": 101,\n" +
                                                    "  \"sessionDate\": \"2026-04-20T10:00:00\"\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "400",
                    description = "Invalid schedule ID or session date provided",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Bad Request - Invalid ID",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-16T16:50:00\",\n" +
                                                    "  \"message\": \"Schedule ID must be positive\",\n" +
                                                    "  \"errorCode\": \"TES-400\",\n" +
                                                    "  \"path\": \"/schedule/0/session-date?sessionDate=2026-04-20T10:00:00\"\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "404",
                    description = "Schedule not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Not Found Response",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-16T16:52:00\",\n" +
                                                    "  \"message\": \"Schedule with id=999 not found\",\n" +
                                                    "  \"errorCode\": \"TES-404\",\n" +
                                                    "  \"path\": \"/schedule/999/session-date?sessionDate=2026-04-20T10:00:00\"\n" +
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
                                                    "  \"timestamp\": \"2026-04-16T16:53:00\",\n" +
                                                    "  \"message\": \"Failed to update session date\",\n" +
                                                    "  \"errorCode\": \"TES-500\",\n" +
                                                    "  \"path\": \"/schedule/1/session-date?sessionDate=2026-04-20T10:00:00\"\n" +
                                                    "}"
                                    )
                            }
                    )
            )
    })
    @PutMapping("/{scheduleId}/session-date")
    public ScheduleDTO updateSessionDate(@PathVariable("scheduleId") Long scheduleId,
                                         @RequestParam LocalDateTime sessionDate) {
        Schedule schedule = scheduleService.updateSessionDate(scheduleId, sessionDate);
        return ScheduleMapper.toDTO(schedule);
    }

    @Operation(summary = "Get Schedules by Batch ID",
            description = "Retrieves all schedules associated with a given batch ID. " +
                    "If schedules exist, they are returned as a list of ScheduleDTOs. " +
                    "If no schedules exist, the response is still 200 with an empty list. " +
                    "If the batchId is invalid (e.g., less than 1), a 400 error is returned.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Schedules retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ScheduleDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Successful Response with Data",
                                            value = "[\n" +
                                                    "  {\n" +
                                                    "    \"scheduleId\": 1,\n" +
                                                    "    \"batchId\": 101,\n" +
                                                    "    \"sessionDate\": \"2026-04-16T09:00:00\"\n" +
                                                    "  },\n" +
                                                    "  {\n" +
                                                    "    \"scheduleId\": 2,\n" +
                                                    "    \"batchId\": 101,\n" +
                                                    "    \"sessionDate\": \"2026-04-17T14:00:00\"\n" +
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
                                            name = "Bad Request - Invalid Batch ID",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-16T16:55:00\",\n" +
                                                    "  \"message\": \"Batch ID must be positive\",\n" +
                                                    "  \"errorCode\": \"TES-400\",\n" +
                                                    "  \"path\": \"/schedule/batch?id=-1\"\n" +
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
                                                    "  \"timestamp\": \"2026-04-16T16:56:00\",\n" +
                                                    "  \"message\": \"Failed to retrieve schedules by batchId\",\n" +
                                                    "  \"errorCode\": \"TES-500\",\n" +
                                                    "  \"path\": \"/schedule/batch?id=101\"\n" +
                                                    "}"
                                    )
                            }
                    )
            )
    })
    @GetMapping("/batch")
    public List<ScheduleDTO> getSchedulesByBatchId(@RequestParam("id") Long batchId) {
        List<Schedule> schedules = scheduleService.getSchedulesByBatchId(batchId);
        return schedules.stream().map(ScheduleMapper::toDTO).toList();
    }
}
