package com.example.fitness_tracker.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.DecimalMin;

@Entity
@Table(
    name = "exercise_workout",
    uniqueConstraints = @UniqueConstraint(columnNames = {"workout_id","exercise_id"})
)
public class ExerciseWorkout {
    @EmbeddedId
    private ExerciseWorkoutId id = new ExerciseWorkoutId();

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @MapsId("workoutId")
    @JoinColumn(name = "workout_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_ew_workout"))
    private Workout workout;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @MapsId("exerciseId")
    @JoinColumn(name = "exercise_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_ew_exercise"))
    private Exercise exercise;

    // Nullable by design; validation only if provided
    @Min(1)
    @Column
    private Integer sets;

    @Min(1)
    @Column
    private Integer reps;

    @DecimalMin(value = "0.0", inclusive = true)
    @Column
    private Double weight;

    public ExerciseWorkoutId getId() { return id; }
    public void setId(ExerciseWorkoutId id) { this.id = id; }
    public Workout getWorkout() { return workout; }
    public void setWorkout(Workout workout) { this.workout = workout; }
    public Exercise getExercise() { return exercise; }
    public void setExercise(Exercise exercise) { this.exercise = exercise; }
    public Integer getSets() { return sets; }
    public void setSets(Integer sets) { this.sets = sets; }
    public Integer getReps() { return reps; }
    public void setReps(Integer reps) { this.reps = reps; }
    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }
}
