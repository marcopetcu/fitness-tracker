package com.example.fitness_tracker.domain;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
    name = "journal",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "date"})
)
public class Journal {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_journal_user"))
    private User user;

    @Column(nullable = false)
    private LocalDate date;

    @OneToMany(mappedBy = "journal", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<JournalFood> food = new HashSet<>();

    public Long getId() { return id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public Set<JournalFood> getfood() { return food; }
    public void setfood(Set<JournalFood> food) { this.food = food; }
}
