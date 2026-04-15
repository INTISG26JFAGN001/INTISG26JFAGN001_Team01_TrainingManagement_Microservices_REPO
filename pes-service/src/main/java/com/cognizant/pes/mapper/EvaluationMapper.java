package com.cognizant.pes.mapper;

import com.cognizant.pes.domain.Evaluation;
import com.cognizant.pes.dto.request.EvaluationRequestDTO;
import com.cognizant.pes.dto.response.EvaluationResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface EvaluationMapper {

    EvaluationMapper INSTANCE = Mappers.getMapper(EvaluationMapper.class);

    Evaluation toDomain(EvaluationRequestDTO dto);

    EvaluationResponseDTO toDto(Evaluation entity);
}