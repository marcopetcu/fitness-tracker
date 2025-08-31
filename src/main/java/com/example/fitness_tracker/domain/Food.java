package com.example.fitness_tracker.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "food")
public class Food {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_food_category"))
    @NotNull(message = "Category is required")
    private FoodCategory category;

    @Min(0) @Column(nullable = false) private Integer kcal = 0;
    @Min(0) @Column(nullable = false) private Double fat = 0.0;
    @Min(0) @Column(nullable = false) private Double carbs = 0.0;
    @Min(0) @Column(nullable = false) private Double protein = 0.0;

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
