package com.cognizant.pes.mapper;

import com.cognizant.pes.domain.Review;
import com.cognizant.pes.dto.ReviewRequestDTO;
import com.cognizant.pes.dto.ReviewResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public interface ReviewMapper {
    ReviewResponseDTO toDto(Review review);

    Review toDomain(ReviewRequestDTO dto);

}
