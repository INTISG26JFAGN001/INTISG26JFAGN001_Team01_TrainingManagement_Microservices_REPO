package com.cognizant.tes.mapper;

import com.cognizant.tes.dto.BatchDTO;
import com.cognizant.tes.dto.BatchDetailsDTO;
import com.cognizant.tes.entity.Batch;

import java.util.List;

public class BatchMapper {

    public static BatchDTO toDTO(Batch batch, List<Long> courseIds) {
        if (batch == null) return null;
        BatchDTO dto = new BatchDTO();
        dto.setId(batch.getBatchId());
        dto.setTrainerId(batch.getTrainerId());
        dto.setStatus(batch.getStatus());
        dto.setCourseIds(courseIds);
        return dto;
    }

    public static BatchDetailsDTO toDetailsDTO(Batch batch,List<Long> courseIds) {
        if (batch == null) return null;
        BatchDetailsDTO dto = new BatchDetailsDTO();
        dto.setId(batch.getBatchId());
        dto.setTrainerId(batch.getTrainerId());
        dto.setStatus(batch.getStatus());
        dto.setStartDate(batch.getStartDate());
        dto.setEndDate(batch.getEndDate());
        dto.setCourseIds(courseIds);
        return dto;
    }

    public static Batch toEntity(BatchDTO dto) {
        if (dto == null) return null;
        Batch batch = new Batch();
        batch.setBatchId(dto.getId());
        batch.setTrainerId(dto.getTrainerId());
        batch.setStatus(dto.getStatus());
        return batch;
    }

    public static Batch toEntity(BatchDetailsDTO dto) {
        if (dto == null) return null;
        Batch batch = new Batch();
        batch.setBatchId(dto.getId());
        batch.setTrainerId(dto.getTrainerId());
        batch.setStatus(dto.getStatus());
        batch.setStartDate(dto.getStartDate());
        batch.setEndDate(dto.getEndDate());
        return batch;
    }
}
