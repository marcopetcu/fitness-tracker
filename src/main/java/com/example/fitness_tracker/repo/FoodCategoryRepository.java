package com.example.fitness_tracker.repo;

import com.example.fitness_tracker.domain.FoodCategory;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodCategoryRepository extends JpaRepository<FoodCategory, Long> {
    boolean existsByName(String name);
    Optional<FoodCategory> findByName(String name);

}