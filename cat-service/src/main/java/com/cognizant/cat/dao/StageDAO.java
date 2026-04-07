package com.cognizant.cat.dao;

import com.cognizant.cat.entity.Stage;
import com.cognizant.cat.repository.StageRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StageDAO {

    private final StageRepository repo;

    public StageDAO(StageRepository repo) {
        this.repo=repo;
    }

    public Stage save(Stage stage) {
        return repo.save(stage);
    }

    public List<Stage> findAll() {
        return repo.findAll();
    }

    public Stage findById(Long id) {
        return repo.findById(id).orElse(null);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
