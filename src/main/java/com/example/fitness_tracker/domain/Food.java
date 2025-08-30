package com.example.fitness_tracker.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "foods")
public class Food {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private FoodCategory category;

    @Min(0)
    private Integer kcal;

    @Min(0)
    private Double fat;

    @Min(0)
    private Double carbs;

    @Min(0)
    private Double protein;

    @PrePersist @PreUpdate
    public void autoComputeKcal() {
        double f = fat == null ? 0 : fat;
        double c = carbs == null ? 0 : carbs;
        double p = protein == null ? 0 : protein;
        this.kcal = (int) Math.round(9 * f + 4 * c + 4 * p);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public FoodCategory getCategory() { return category; }
    public void setCategory(FoodCategory category) { this.category = category; }
    public Integer getKcal() { return kcal; }
    public void setKcal(Integer kcal) { this.kcal = kcal; }
    public Double getFat() { return fat; }
    public void setFat(Double fat) { this.fat = fat; }
    public Double getCarbs() { return carbs; }
    public void setCarbs(Double carbs) { this.carbs = carbs; }
    public Double getProtein() { return protein; }
    public void setProtein(Double protein) { this.protein = protein; }
}
