package com.example.fitness_tracker.repo;

import com.example.fitness_tracker.domain.Workout;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface WorkoutRepository extends JpaRepository<Workout, Long> {
    Optional<Workout> findByUserIdAndDate(Long userId, LocalDate date);
}
