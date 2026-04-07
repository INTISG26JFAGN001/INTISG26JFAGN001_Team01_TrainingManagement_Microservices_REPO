package com.cognizant.cat.dao;

import com.cognizant.cat.entity.Technology;
import com.cognizant.cat.repository.TechnologyRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TechnologyDAO {

    private final TechnologyRepository repo;

    public TechnologyDAO(TechnologyRepository repo) {
        this.repo=repo;
    }

    public Technology save(Technology tech) {
        return repo.save(tech);
    }

    public List<Technology> findAll() {
        return repo.findAll();
    }

    public Technology findById(Long id) {
        return repo.findById(id).orElse(null);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
