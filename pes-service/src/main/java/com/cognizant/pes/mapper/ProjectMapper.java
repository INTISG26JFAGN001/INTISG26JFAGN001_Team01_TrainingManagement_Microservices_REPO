package com.cognizant.pes.mapper;

import com.cognizant.pes.domain.Project;
import com.cognizant.pes.dto.request.ProjectRequestDTO;
import com.cognizant.pes.dto.response.ProjectResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public interface ProjectMapper {
    ProjectResponseDTO toDto(Project project);

    Project toDomain(ProjectRequestDTO dto);
}
