package com.cognizant.pes.repository;

import com.cognizant.pes.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IReviewRepository extends JpaRepository<Review,Long> {
    List<Review> findByProjectId(Long projectId);

    List<Review> findByReviewerId(Long reviewerId);


}
