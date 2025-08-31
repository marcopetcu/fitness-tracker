package com.example.fitness_tracker.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ExerciseForm {
    @NotBlank
    private String name;

    @NotNull
    private Long muscleGroupId;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Long getMuscleGroupId() { return muscleGroupId; }
    public void setMuscleGroupId(Long muscleGroupId) { this.muscleGroupId = muscleGroupId; }
}
