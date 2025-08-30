package com.example.fitness_tracker.domain;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "user_details")
public class UserDetailsProfile {
    @Id
    private Long id; // = app_user.id

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_userdetails_user"))
    private User user;

    private LocalDate birthday;
    private Double height;
    private Double weight;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public LocalDate getBirthday() { return birthday; }
    public void setBirthday(LocalDate birthday) { this.birthday = birthday; }
    public Double getHeight() { return height; }
    public void setHeight(Double height) { this.height = height; }
    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }
}
