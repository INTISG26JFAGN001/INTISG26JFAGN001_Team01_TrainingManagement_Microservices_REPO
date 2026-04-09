package com.cognizant.pes.service;

import com.cognizant.pes.dto.ReviewRequestDTO;
import com.cognizant.pes.dto.ReviewResponseDTO;
import jakarta.validation.Valid;

import java.util.List;

public interface IReviewService {
    public ReviewResponseDTO saveReview(Long projectId, ReviewRequestDTO request);
    public ReviewResponseDTO getResponseById(Long reviewId);

    ReviewResponseDTO updateReview(Long reviewId, @Valid ReviewRequestDTO request);

    void deleteReview(Long reviewId);

    List<ReviewResponseDTO> getReviewsByProject(Long projectId);

    List<ReviewResponseDTO> getReviewsByReviewer(Long reviewerId);

    ReviewResponseDTO getReviewById(Long reviewId);
}
