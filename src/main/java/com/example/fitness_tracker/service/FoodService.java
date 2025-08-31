package com.example.fitness_tracker.service;

import com.example.fitness_tracker.domain.Food;
import com.example.fitness_tracker.exception.NotFoundException;
import com.example.fitness_tracker.repo.FoodRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FoodService {
    private final FoodRepository repo;

    public FoodService(FoodRepository repo) { this.repo = repo; }

    public Page<Food> list(String q, Pageable pageable) {
        if (q == null || q.isBlank()) {
            log.debug("FoodService.list all page={}", pageable);
            return repo.findAll(pageable);
        } else {
            log.debug("FoodService.list q='{}' page={}", q, pageable);
            return repo.findByNameContainingIgnoreCase(q, pageable);
        }
    }

    public Food get(Long id) {
        return repo.findById(id).orElseThrow(() -> {
            log.warn("FoodService.get not found id={}", id);
            return new NotFoundException("Food " + id + " not found");
        });
    }

    public Food save(Food f) {
        boolean isNew = (f.getId() == null);
        Food saved = repo.save(f);
        if (isNew) {
            log.info("FoodService.save created id={} name='{}'", saved.getId(), saved.getName());
        } else {
            log.info("FoodService.save updated id={} name='{}'", saved.getId(), saved.getName());
        }
        return saved;
    }

    public void delete(Long id) {
        repo.deleteById(id);
        log.info("FoodService.delete id={}", id);
    }
}