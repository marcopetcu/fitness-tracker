package com.example.fitness_tracker.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "exercise", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Exercise {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String name;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "muscle_group_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_exercise_musclegroup"))
    @NotNull
    private MuscleGroup muscleGroup;

    // backref pentru M:M pur cu User.favoriteExercises
    @ManyToMany(mappedBy = "favoriteExercises")
    private Set<User> favoredBy = new HashSet<>();

    // backref către join-entity Workout<->Exercise
    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ExerciseWorkout> workouts = new HashSet<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public MuscleGroup getMuscleGroup() { return muscleGroup; }
    public void setMuscleGroup(MuscleGroup muscleGroup) { this.muscleGroup = muscleGroup; }
    public Set<User> getFavoredBy() { return favoredBy; }
    public void setFavoredBy(Set<User> favoredBy) { this.favoredBy = favoredBy; }
    public Set<ExerciseWorkout> getWorkouts() { return workouts; }
    public void setWorkouts(Set<ExerciseWorkout> workouts) { this.workouts = workouts; }
}
