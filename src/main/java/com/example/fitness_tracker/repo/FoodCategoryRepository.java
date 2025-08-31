package com.example.fitness_tracker.repo;

import com.example.fitness_tracker.domain.FoodCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodCategoryRepository extends JpaRepository<FoodCategory, Long> {
    boolean existsByName(String name);
}