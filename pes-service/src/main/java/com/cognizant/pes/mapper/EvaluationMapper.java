package com.cognizant.pes.mapper;

import com.cognizant.pes.domain.Evaluation;
import com.cognizant.pes.dto.EvaluationRequestDTO;
import com.cognizant.pes.dto.EvaluationResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface EvaluationMapper {

    EvaluationMapper INSTANCE = Mappers.getMapper(EvaluationMapper.class);

    // Convert Request DTO -> Entity
    Evaluation toDomain(EvaluationRequestDTO dto);

    // Convert Entity -> Response DTO
    EvaluationResponseDTO toDto(Evaluation entity);
}