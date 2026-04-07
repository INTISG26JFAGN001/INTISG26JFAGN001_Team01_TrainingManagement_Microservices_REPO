package com.cognizant.cat.dao;

import com.cognizant.cat.entity.Course;
import com.cognizant.cat.repository.CourseRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CourseDAO {

    private final CourseRepository repo;
    public CourseDAO(CourseRepository repo) {
        this.repo=repo;
    }

    public Course save(Course course) {
        return repo.save(course);
    }

    public List<Course> findAll() {
        return repo.findAll();
    }

    public Course findById(Long id) {
        return repo.findById(id).orElse(null);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
