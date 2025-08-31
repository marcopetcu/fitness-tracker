package com.example.fitness_tracker.repo;

import com.example.fitness_tracker.domain.ExerciseWorkout;
import com.example.fitness_tracker.domain.ExerciseWorkoutId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseWorkoutRepository extends JpaRepository<ExerciseWorkout, ExerciseWorkoutId> {
}
