package com.cognizant.tes.mapper;

import com.cognizant.tes.dto.AssociateDTO;
import com.cognizant.tes.dto.CreateAssociateDTO;
import com.cognizant.tes.entity.Associate;

public class AssociateMapper {
    public static AssociateDTO toDTO(Associate associate) {
        if (associate == null) {
            return null;
        }
        AssociateDTO dto = new AssociateDTO();
        dto.setId(associate.getId());
        dto.setUserId(associate.getUserId());
        dto.setBatchId(associate.getBatchId());
        dto.setXp(associate.getXp());
        return dto;
    }

    public static Associate toEntity(AssociateDTO dto) {
        if (dto == null) {
            return null;
        }
        Associate associate = new Associate();
        associate.setId(dto.getId());
        associate.setUserId(dto.getUserId());
        associate.setBatchId(dto.getBatchId());
        associate.setXp(dto.getXp());
        return associate;
    }
    public static Associate toEntity(CreateAssociateDTO dto) {
        if (dto == null) {
            return null;
        }
        Associate associate = new Associate();
        associate.setUserId(dto.getUserId());
        associate.setBatchId(dto.getBatchid());
        associate.setXp(dto.getXp());
        return associate;
    }
}
