package com.example.fitness_tracker.repo;

import com.example.fitness_tracker.domain.Exercise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    Page<Exercise> findByNameContainingIgnoreCase(String q, Pageable pageable);
}
