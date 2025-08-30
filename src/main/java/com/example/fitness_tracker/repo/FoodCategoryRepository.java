package com.example.fitness_tracker.repo;

import com.example.fitness_tracker.domain.FoodCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FoodCategoryRepository extends JpaRepository<FoodCategory, Long> {
    Optional<FoodCategory> findByName(String name);
}