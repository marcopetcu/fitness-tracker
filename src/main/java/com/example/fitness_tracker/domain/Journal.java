package com.example.fitness_tracker.domain;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "journals", uniqueConstraints = @UniqueConstraint(columnNames = {"username","date"}))
public class Journal {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private LocalDate date;

    @OneToMany(mappedBy = "journal", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<JournalFood> foods = new HashSet<>();

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public Set<JournalFood> getFoods() { return foods; }
    public void setFoods(Set<JournalFood> foods) { this.foods = foods; }
}