package com.cognizant.tes.controller;

import com.cognizant.tes.client.ICourseServiceClient;
import com.cognizant.tes.dao.ICourseBatchMapDAO;
import com.cognizant.tes.dto.BatchDTO;
import com.cognizant.tes.dto.BatchDetailsDTO;
import com.cognizant.tes.dto.CourseResponseDTO;
import com.cognizant.tes.entity.Batch;
import com.cognizant.tes.entity.BatchStatus;
import com.cognizant.tes.mapper.BatchMapper;
import com.cognizant.tes.service.IBatchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/batches")
public class BatchController {

    private final IBatchService batchService;
    private final ICourseBatchMapDAO courseBatchMapDAO;
    private final ICourseServiceClient courseServiceClient;

    public BatchController(IBatchService batchService, ICourseBatchMapDAO courseBatchMapDAO,
                           ICourseServiceClient courseServiceClient) {
        this.courseServiceClient = courseServiceClient;
        this.batchService = batchService;
        this.courseBatchMapDAO = courseBatchMapDAO;
    }

    @PostMapping
    public ResponseEntity<BatchDetailsDTO> createBatch(@RequestBody BatchDetailsDTO requestDto) {

        Batch savedBatch = batchService.addBatch(requestDto);


        BatchDetailsDTO responseDto = new BatchDetailsDTO();
        responseDto.setId(savedBatch.getBatchId());
        responseDto.setTrainerId(savedBatch.getTrainerId());
        responseDto.setStatus(savedBatch.getStatus());

        List<String> courseNames = requestDto.getCourseIds().stream()
                .map(courseServiceClient::getCourseById)
                .map(CourseResponseDTO::getTitle)
                .collect(Collectors.toList());

        responseDto.setCourseNames(courseNames);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<BatchDTO>> getAllBatches() {
        List<BatchDTO> batchDTOs = batchService.getAllBatches().stream()
                .map(batch -> {
                    List<Long> courseIds = courseBatchMapDAO.findCourseIdsByBatchId(batch.getBatchId());
                    List<String> courseNames = courseIds.stream()
                            .map(courseServiceClient::getCourseById)
                            .map(CourseResponseDTO::getTitle)
                            .collect(Collectors.toList());

                    BatchDTO dto = new BatchDTO();
                    dto.setId(batch.getBatchId());
                    dto.setTrainerId(batch.getTrainerId());
                    dto.setStatus(batch.getStatus());
                    dto.setCourseNames(courseNames);
                    return dto;
                })
                .toList();

        return ResponseEntity.ok(batchDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BatchDTO> getBatchById(@PathVariable Long id) {
        Batch batch = batchService.getBatchById(id);

        List<Long> courseIds = courseBatchMapDAO.findCourseIdsByBatchId(batch.getBatchId());
        List<String> courseNames = courseIds.stream()
                .map(courseServiceClient::getCourseById)
                .map(CourseResponseDTO::getTitle)
                .collect(Collectors.toList());

        BatchDTO dto = new BatchDTO();
        dto.setId(batch.getBatchId());
        dto.setTrainerId(batch.getTrainerId());
        dto.setStatus(batch.getStatus());
        dto.setCourseNames(courseNames);

        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BatchDTO> deleteBatch(@PathVariable Long id) {
        Batch deletedBatch = batchService.deleteBatch(id);
        return ResponseEntity.ok(BatchMapper.toDTO(deletedBatch, getCourseIds(deletedBatch.getBatchId())));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<BatchDTO> updateBatchStatus(@PathVariable Long id, @RequestParam BatchStatus status) {
        Batch updatedBatch = batchService.updateStatusById(id, status);
        return ResponseEntity.ok(BatchMapper.toDTO(updatedBatch, getCourseIds(updatedBatch.getBatchId())));
    }

    @GetMapping("/status")
    public ResponseEntity<List<BatchDTO>> getBatchesByStatus(@RequestParam("status") BatchStatus status) {
        List<BatchDTO> batchDTOs = batchService.getBatchesByStatus(status).stream()
                .map(batch -> BatchMapper.toDTO(batch, getCourseIds(batch.getBatchId())))
                .toList();
        return ResponseEntity.ok(batchDTOs);
    }

    @GetMapping("/course")
    public ResponseEntity<List<BatchDTO>> getBatchesByCourseId(@RequestParam("course_id") Long courseId) {
        List<BatchDTO> batchDTOs = batchService.getBatchesByCourseId(courseId).stream()
                .map(batch -> BatchMapper.toDTO(batch, getCourseIds(batch.getBatchId())))
                .toList();
        return ResponseEntity.ok(batchDTOs);
    }

    @GetMapping("/trainer")
    public ResponseEntity<List<BatchDTO>> getBatchesByTrainerId(@RequestParam("trainer_id") Long trainerId) {
        List<BatchDTO> batchDTOs = batchService.getBatchByTrainerId(trainerId).stream()
                .map(batch -> BatchMapper.toDTO(batch, getCourseIds(batch.getBatchId())))
                .toList();
        return ResponseEntity.ok(batchDTOs);
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<BatchDetailsDTO> getBatchDetails(@PathVariable Long id) {
        Batch batchDetails = batchService.getBatchDetailsById(id);

        List<Long> courseIds = courseBatchMapDAO.findCourseIdsByBatchId(batchDetails.getBatchId());
        List<String> courseNames = courseIds.stream()
                .map(courseServiceClient::getCourseById)
                .map(CourseResponseDTO::getTitle)
                .collect(Collectors.toList());

        BatchDetailsDTO dto = new BatchDetailsDTO();
        dto.setId(batchDetails.getBatchId());
        dto.setTrainerId(batchDetails.getTrainerId());
        dto.setStatus(batchDetails.getStatus());
        dto.setCourseNames(courseNames);

        return ResponseEntity.ok(dto);
    }
    private List<Long> getCourseIds(Long batchId) {
        return courseBatchMapDAO.findCourseIdsByBatchId(batchId);
    }

    @GetMapping("/{batchId}/courses")
    public ResponseEntity<List<CourseResponseDTO>> getCoursesForBatch(@PathVariable Long batchId) {
        List<CourseResponseDTO> courses = batchService.getCoursesForBatch(batchId);
        return ResponseEntity.ok(courses);
    }
}
