package com.cognizant.pes.dao.impl;

import com.cognizant.pes.dao.IReviewDAO;
import com.cognizant.pes.domain.Review;
import com.cognizant.pes.repository.IReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReviewDAOImpl implements IReviewDAO {

    @Autowired
    private IReviewRepository reviewRepository;

    @Override
    public Review save(Review review) {
        return reviewRepository.save(review);
    }

    @Override
    public Review update(Review existingReview) {
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




    public void deleteById(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }
}