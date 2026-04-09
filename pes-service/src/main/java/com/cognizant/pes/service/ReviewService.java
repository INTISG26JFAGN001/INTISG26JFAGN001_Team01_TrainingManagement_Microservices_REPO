package com.cognizant.pes.service;

import com.cognizant.pes.dao.ReviewDAOImpl;
import com.cognizant.pes.dto.ReviewRequestDTO;
import com.cognizant.pes.dto.ReviewResponseDTO;
import com.cognizant.pes.domain.Review;
import com.cognizant.pes.mapper.ReviewMapper; // Assuming your mapper name
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService implements IReviewService {

    @Autowired
    private ReviewDAOImpl reviewDAO;

    @Autowired
    private ReviewMapper reviewMapper; // Injecting your mapper class

    @Override
    @Transactional
    public ReviewResponseDTO saveReview(Long projectId, ReviewRequestDTO request) {
        // Use mapper to convert DTO to Entity [cite: 50, 52]
        Review review = reviewMapper.toDomain(request);
        review.setProjectId(projectId); // Ensure ID from URL is set

        Review savedReview = reviewDAO.save(review);

        // KAFKA: Topic project.events -> Publish ReviewCompleted (ProjectSubmitted/ReviewCompleted) [cite: 63, 103]

        return reviewMapper.toDto(savedReview);
    }

    @Override
    @Transactional
    public ReviewResponseDTO updateReview(Long reviewId, ReviewRequestDTO request) {
        Review existingReview = reviewDAO.findById(reviewId);
        if (existingReview != null) {
            // Update fields from request DTO to existing entity


            Review updatedReview = reviewDAO.update(reviewMapper.toDomain(request));

            // KAFKA: Topic project.events -> Publish EvaluationUpdated [cite: 63, 103]

            return reviewMapper.toDto(updatedReview);
        }
        return null;
    }

    @Override
    public ReviewResponseDTO getResponseById(Long reviewId) {
        Review review = reviewDAO.findById(reviewId);
        return (review != null) ? reviewMapper.toDto(review) : null;
    }

    @Override
    public List<ReviewResponseDTO> getReviewsByProject(Long projectId) {
        return reviewDAO.findByProjectId(projectId)
                .stream()
                .map(reviewMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReviewResponseDTO> getReviewsByReviewer(Long reviewerId) {
        return reviewDAO.findByReviewerId(reviewerId)
                .stream()
                .map(reviewMapper::toDto)
                .collect(Collectors.toList());
    }


    @Override
    public ReviewResponseDTO getReviewById(Long reviewId) {
        return null;
    }

    @Override
    @Transactional
    public void deleteReview(Long reviewId) {
        reviewDAO.deleteById(reviewId);
    }
}