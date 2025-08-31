package com.example.fitness_tracker.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
    name = "workout",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "date"})
)
public class Workout {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_workout_user"))
    private User user;

    @NotNull
    @Column(nullable = false)
    private LocalDate date;

    @OneToMany(mappedBy = "workout", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ExerciseWorkout> exercises = new HashSet<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public Set<ExerciseWorkout> getExercises() { return exercises; }
    public void setExercises(Set<ExerciseWorkout> exercises) { this.exercises = exercises; }
}
