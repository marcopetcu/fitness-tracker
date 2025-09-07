package com.example.fitness_tracker.repo;

import com.example.fitness_tracker.domain.MuscleGroup;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MuscleGroupRepository extends JpaRepository<MuscleGroup, Long> {
    boolean existsByName(String name);
    Optional<MuscleGroup> findByName(String name);

}