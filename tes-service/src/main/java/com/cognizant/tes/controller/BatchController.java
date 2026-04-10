package com.cognizant.tes.controller;

import com.cognizant.tes.dao.ICourseBatchMapDAO;
import com.cognizant.tes.dto.BatchDTO;
import com.cognizant.tes.dto.BatchDetailsDTO;
import com.cognizant.tes.entity.Batch;
import com.cognizant.tes.entity.BatchStatus;
import com.cognizant.tes.mapper.BatchMapper;
import com.cognizant.tes.service.IBatchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/batches")
public class BatchController {

    private final IBatchService batchService;
    private final ICourseBatchMapDAO courseBatchMapDAO;

    public BatchController(IBatchService batchService, ICourseBatchMapDAO courseBatchMapDAO) {
        this.batchService = batchService;
        this.courseBatchMapDAO = courseBatchMapDAO;
    }

    @PostMapping
    public ResponseEntity<BatchDetailsDTO> createBatch(@RequestBody BatchDetailsDTO batchDetailsDTO) {
        Batch createdBatch = batchService.addBatch(batchDetailsDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BatchMapper.toDetailsDTO(createdBatch, getCourseIds(createdBatch.getBatchId())));
    }

    @GetMapping
    public ResponseEntity<List<BatchDTO>> getAllBatches() {
        List<BatchDTO> batchDTOs = batchService.getAllBatches().stream()
                .map(batch -> BatchMapper.toDTO(batch, getCourseIds(batch.getBatchId())))
                .toList();
        return ResponseEntity.ok(batchDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BatchDTO> getBatchById(@PathVariable Long id) {
        Batch batch = batchService.getBatchById(id);
        return ResponseEntity.ok(BatchMapper.toDTO(batch, getCourseIds(batch.getBatchId())));
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
        return ResponseEntity.ok(BatchMapper.toDetailsDTO(batchDetails, getCourseIds(batchDetails.getBatchId())));
    }

    private List<Long> getCourseIds(Long batchId) {
        return courseBatchMapDAO.findCourseIdsByBatchId(batchId);
    }
}
