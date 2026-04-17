package com.cognizant.pes.service.impl;

import com.cognizant.pes.dao.impl.ProjectDAOImpl;
import com.cognizant.pes.dao.impl.ReviewDAOImpl;
import com.cognizant.pes.domain.Project;
import com.cognizant.pes.dto.request.ReviewRequestDTO;
import com.cognizant.pes.dto.response.ReviewResponseDTO;
import com.cognizant.pes.domain.Review;
import com.cognizant.pes.exception.ResourceNotFoundException;
import com.cognizant.pes.mapper.ReviewMapper;
import com.cognizant.pes.service.IReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ReviewService implements IReviewService {

    @Autowired
    private ReviewDAOImpl reviewDAO;

    @Autowired
    private ProjectDAOImpl projectDAO;

    @Autowired
    private ReviewMapper reviewMapper;

    @Override
    @Transactional
    public ReviewResponseDTO saveReview(Long projectId, ReviewRequestDTO request) throws ResourceNotFoundException {

        log.info("Creating a new review for project ID: {}", projectId);

        Project project = projectDAO.findById(projectId);

        if (project == null) {
            log.error("Project with ID: {} does not exist", projectId);
            throw new ResourceNotFoundException(
                    "Project not found with id: " + projectId);
        }
        Review review = reviewMapper.toDomain(request);

        review.setProject(project);

        Review savedReview = reviewDAO.save(review);

        log.info(
                "Successfully saved review with ID: {} for project: {}",
                savedReview.getId(), projectId
        );

        return reviewMapper.toDto(savedReview);
    }

    @Override
    @Transactional
    public ReviewResponseDTO updateReview(Long reviewId, ReviewRequestDTO request) {
        log.info("Request to update review ID: {}", reviewId);

        Review existingReview = reviewDAO.findById(reviewId);
        if (existingReview != null) {
            Review updatedReview = reviewDAO.update(reviewMapper.toDomain(request));


            return reviewMapper.toDto(updatedReview);
        }

        log.warn("Update failed: Review with ID: {} does not exist", reviewId);
        return null;
    }

    @Override
    public ReviewResponseDTO getResponseById(Long reviewId) {
        log.debug("Fetching response for review ID: {}", reviewId);
        Review review = reviewDAO.findById(reviewId);
        return (review != null) ? reviewMapper.toDto(review) : null;
    }

    @Override
    public List<ReviewResponseDTO> getReviewsByProject(Long projectId) {
        log.info("Fetching all reviews for project ID: {}", projectId);
        List<Review> reviews = reviewDAO.findByProjectId(projectId);
        log.debug("Found {} reviews for project ID: {}", reviews.size(), projectId);

        return reviews.stream()
                .map(reviewMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReviewResponseDTO> getReviewsByReviewer(Long reviewerId) {
        log.info("Fetching reviews for reviewer ID: {}", reviewerId);
        return reviewDAO.findByReviewerId(reviewerId)
                .stream()
                .map(reviewMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ReviewResponseDTO getReviewById(Long reviewId) {
        log.debug("GET request for review ID: {}", reviewId);
        Review review = reviewDAO.findById(reviewId);
        return (review != null) ? reviewMapper.toDto(review) : null;
    }

    @Override
    @Transactional
    public void deleteReview(Long reviewId) {
        log.info("Attempting to delete review ID: {}", reviewId);
        reviewDAO.deleteById(reviewId);
        log.info("Review ID: {} successfully deleted", reviewId);
    }
}