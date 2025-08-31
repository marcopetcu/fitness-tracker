package com.example.fitness_tracker.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
    name = "app_user",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
    }
)
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 80)
    private String username;

    @Email @NotBlank
    @Column(nullable = false, length = 150)
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean enabled = true;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_user_role"))
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Workout> workouts = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "user_favorite_exercise",
        joinColumns = @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_fav_user")),
        inverseJoinColumns = @JoinColumn(name = "exercise_id", foreignKey = @ForeignKey(name = "fk_fav_exercise"))
    )
    private Set<Exercise> favoriteExercises = new HashSet<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private UserDetailsProfile details;

    public UserDetailsProfile getDetails() { return details; }
    public void setDetails(UserDetailsProfile details) { this.details = details; }

    // getters/setters standard
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public Set<Workout> getWorkouts() { return workouts; }
    public void setWorkouts(Set<Workout> workouts) { this.workouts = workouts; }

    public Set<Exercise> getFavoriteExercises() { return favoriteExercises; }
    public void setFavoriteExercises(Set<Exercise> favoriteExercises) { this.favoriteExercises = favoriteExercises; }

    // metode ajutătoare (opțional)
    public void addFavoriteExercise(Exercise e) {
        this.favoriteExercises.add(e);
    }
    public void removeFavoriteExercise(Exercise e) {
        this.favoriteExercises.remove(e);
    }
}