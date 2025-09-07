package com.example.fitness_tracker.repo;

import com.example.fitness_tracker.domain.Food;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodRepository extends JpaRepository<Food, Long> {
    Page<Food> findByNameContainingIgnoreCase(String q, Pageable pageable);
    Optional<Food> findByName(String name);

}
