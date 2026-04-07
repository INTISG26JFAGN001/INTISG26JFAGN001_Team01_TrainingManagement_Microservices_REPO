package com.cognizant.cat.repository;

import com.cognizant.cat.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
