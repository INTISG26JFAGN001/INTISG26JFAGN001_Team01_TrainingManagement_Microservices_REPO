package com.cognizant.tes.controller;

import com.cognizant.tes.dto.EnrollmentDTO;
import com.cognizant.tes.entity.Enrollment;
import com.cognizant.tes.entity.EnrollmentStatus;
import com.cognizant.tes.mapper.EnrollmentMapper;
import com.cognizant.tes.service.IEnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enrollment")
public class EnrollmentController {


    private final IEnrollmentService enrollmentService;

    public EnrollmentController(IEnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping
    private ResponseEntity<EnrollmentDTO> createEnrollment(@RequestBody EnrollmentDTO dto){
        Enrollment created = enrollmentService.createEnrollment(dto);
        return ResponseEntity.ok(EnrollmentMapper.toDTO(created));
    }

    @GetMapping("/{id}")
    private ResponseEntity<EnrollmentDTO> getEnrollmentById(@PathVariable Long id){
        Enrollment enrollment = enrollmentService.getEnrollmentById(id);
        return ResponseEntity.ok(EnrollmentMapper.toDTO(enrollment));
    }

    @GetMapping
    private ResponseEntity<List<EnrollmentDTO>> getAllEnrollments(){
        List<Enrollment> enrollments = enrollmentService.getAllEnrollments();
        List<EnrollmentDTO> dtos = enrollments.stream().map(EnrollmentMapper::toDTO).toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/batch")
    private ResponseEntity<List<EnrollmentDTO>> getEnrollmentsByBatch(@RequestParam("id") Long batchId){
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByBatchId(batchId);
        List<EnrollmentDTO> dtos = enrollments.stream().map(EnrollmentMapper::toDTO).toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/associate")
    private ResponseEntity<EnrollmentDTO> getEnrollmentByAssociate(@RequestParam("id") Long associateId){
        Enrollment enrollment = enrollmentService.getEnrollmentByAssociateId(associateId);
        return ResponseEntity.ok(EnrollmentMapper.toDTO(enrollment));
    }

    @PutMapping("/{id}/status")
    private ResponseEntity<EnrollmentDTO> updateStatus(@PathVariable("id") Long id,
                                                       @RequestParam("val") EnrollmentStatus status){
        Enrollment updated = enrollmentService.updateStatus(id, status);
        return ResponseEntity.ok(EnrollmentMapper.toDTO(updated));
    }

    @GetMapping("/status")
    private ResponseEntity<List<EnrollmentDTO>> getEnrollmentsByStatus(@RequestParam("val") EnrollmentStatus status){
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByStatus(status);
        List<EnrollmentDTO> dtos = enrollments.stream().map(EnrollmentMapper::toDTO).toList();
        return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<EnrollmentDTO> deleteEnrollment(@PathVariable Long id){
        Enrollment deleted = enrollmentService.deleteEnrollment(id);
        return ResponseEntity.ok(EnrollmentMapper.toDTO(deleted));
    }
}
