package com.cognizant.pes.dao;

import com.cognizant.pes.domain.Review;
import com.cognizant.pes.repository.IReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReviewDAOImpl implements IReviewDAO {

    @Autowired
    private IReviewRepository reviewRepository;

    @Override
    public Review save(Review review) {
        // JPA handles ID generation and persistence [cite: 18]
        return reviewRepository.save(review);
    }

    @Override
    public Review update(Review existingReview) {
        // In JPA, save() acts as an update if the ID is present
        return reviewRepository.save(existingReview);
    }

    @Override
    public Review findById(Long reviewId) {
        return reviewRepository.findById(reviewId).orElse(null);
    }

    @Override
    public List<Review> findByProjectId(Long projectId) {
        return reviewRepository.findByProjectId(projectId);
    }

    @Override
    public List<Review> findByReviewerId(Long reviewerId) {
        return reviewRepository.findByReviewerId(reviewerId);
    }



    // Supporting the service layer's delete requirement
    public void deleteById(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }
}