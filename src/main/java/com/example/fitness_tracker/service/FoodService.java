package com.example.fitness_tracker.service;

import com.example.fitness_tracker.domain.Food;
import com.example.fitness_tracker.exception.NotFoundException;
import com.example.fitness_tracker.repo.FoodRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class FoodService {
    private final FoodRepository repo;

    public FoodService(FoodRepository repo) { this.repo = repo; }

    public Page<Food> list(String q, Pageable pageable) {
        return (q == null || q.isBlank()) ? repo.findAll(pageable)
                                          : repo.findByNameContainingIgnoreCase(q, pageable);
    }

    public Food get(Long id) {
        return repo.findById(id).orElseThrow(() -> new NotFoundException("Food " + id + " not found"));
    }

    public Food save(Food f) { return repo.save(f); }

    public void delete(Long id) { repo.deleteById(id); }
}