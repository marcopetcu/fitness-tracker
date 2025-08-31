package com.example.fitness_tracker.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ExerciseWorkoutId implements Serializable {
    @Column(name = "workout_id")
    private Long workoutId;

    @Column(name = "exercise_id")
    private Long exerciseId;

    public ExerciseWorkoutId() {}
    public ExerciseWorkoutId(Long workoutId, Long exerciseId) {
        this.workoutId = workoutId; this.exerciseId = exerciseId;
    }

    public Long getWorkoutId() { return workoutId; }
    public void setWorkoutId(Long workoutId) { this.workoutId = workoutId; }
    public Long getExerciseId() { return exerciseId; }
    public void setExerciseId(Long exerciseId) { this.exerciseId = exerciseId; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExerciseWorkoutId that)) return false;
        return Objects.equals(workoutId, that.workoutId)
            && Objects.equals(exerciseId, that.exerciseId);
    }
    @Override public int hashCode() { return Objects.hash(workoutId, exerciseId); }
}
