package com.cognizant.pes.dao;

import com.cognizant.pes.domain.Review;

import java.util.Arrays;
import java.util.List;

public interface IReviewDAO {
    Review save(Review review);

    Review findById(Long reviewId);

    Review update(Review existingReview);

    List<Review> findByProjectId(Long projectId);

    List<Review> findByReviewerId(Long reviewerId);

}
