package com.example.fitness_tracker.web.dto;

import jakarta.validation.constraints.NotNull;

public class AddExerciseToWorkoutForm {
    @NotNull
    private Long exerciseId;

    private Integer sets;
    private Integer reps;
    private Double weight;

    public Long getExerciseId() { return exerciseId; }
    public void setExerciseId(Long exerciseId) { this.exerciseId = exerciseId; }
    public Integer getSets() { return sets; }
    public void setSets(Integer sets) { this.sets = sets; }
    public Integer getReps() { return reps; }
    public void setReps(Integer reps) { this.reps = reps; }
    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }
}