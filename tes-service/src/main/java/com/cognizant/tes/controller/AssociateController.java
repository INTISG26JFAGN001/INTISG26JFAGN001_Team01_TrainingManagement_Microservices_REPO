package com.cognizant.tes.controller;

import com.cognizant.tes.dto.AssociateDTO;
import com.cognizant.tes.dto.CreateAssociateDTO;
import com.cognizant.tes.entity.Associate;
import com.cognizant.tes.mapper.AssociateMapper;
import com.cognizant.tes.service.IAssociateService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/associates")
public class AssociateController {
    private final IAssociateService associateService;;

    @Autowired
    public AssociateController(IAssociateService associateService){
        this.associateService = associateService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<AssociateDTO> getAssociateByUserId(@PathVariable long userId){
        Associate associate = associateService.getByUserId(userId);
        AssociateDTO associateDTO = AssociateMapper.toDTO(associate);
        return ResponseEntity.ok(associateDTO);
    }

    @GetMapping
    public ResponseEntity<List<AssociateDTO>> getAllAssociates(){
        List<Associate> associates = associateService.getAll();
        List<AssociateDTO> associateDTOs = associates.stream().map(AssociateMapper::toDTO).toList();
        return ResponseEntity.ok(associateDTOs);
    }

    @GetMapping("/batch")
    public ResponseEntity<List<AssociateDTO>> getAssociatesByBatchId(@RequestParam("id") long batchId){
        List<Associate> associates = associateService.getByBatchId(batchId);
        List<AssociateDTO> associateDTOs = associates.stream().map(AssociateMapper::toDTO).toList();
        return ResponseEntity.ok(associateDTOs);
    }

    @GetMapping("/xp")
    public ResponseEntity<List<AssociateDTO>> getAssociatesByCourseId(@RequestParam("value") int xp){
        List<Associate> associates = associateService.getByXp(xp);
        List<AssociateDTO> associateDTOs = associates.stream().map(AssociateMapper::toDTO).toList();
        return ResponseEntity.ok(associateDTOs);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createAssociate(@Valid @RequestBody CreateAssociateDTO associateDTO) {
        Associate associate = AssociateMapper.toEntity(associateDTO);
        boolean result = associateService.create(associate);
        if (result) {
            return ResponseEntity.ok("Associate created successfully");
        } else {
            return ResponseEntity.status(500).body("Failed to create associate");
        }
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateAssociate(@Valid @RequestBody AssociateDTO associateDTO) {
        Associate associate = AssociateMapper.toEntity(associateDTO);
        Associate updatedAssociate = associateService.update(associate);
        return ResponseEntity.ok("Associate updated successfully");
    }

}
