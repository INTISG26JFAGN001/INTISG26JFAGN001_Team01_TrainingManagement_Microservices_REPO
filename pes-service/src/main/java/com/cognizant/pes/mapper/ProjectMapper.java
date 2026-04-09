package com.cognizant.pes.mapper;

import com.cognizant.pes.domain.Project;
import com.cognizant.pes.dto.ProjectRequestDTO;
import com.cognizant.pes.dto.ProjectResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public interface ProjectMapper {
    ProjectResponseDTO toDto(Project project);

    Project toDomain(ProjectRequestDTO dto);
}
