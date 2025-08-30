package com.example.fitness_tracker.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "journal_food", uniqueConstraints = @UniqueConstraint(columnNames = {"journal_id","food_id"}))
public class JournalFood {
    @EmbeddedId
    private JournalFoodId id = new JournalFoodId();

    @ManyToOne
    @MapsId("journalId")
    @JoinColumn(name = "journal_id")
    private Journal journal;

    @ManyToOne
    @MapsId("foodId")
    @JoinColumn(name = "food_id")
    private Food food;

    @Column(nullable = false)
    private Integer quantityGrams;

    public JournalFoodId getId() { return id; }
    public Journal getJournal() { return journal; }
    public void setJournal(Journal journal) { this.journal = journal; }
    public Food getFood() { return food; }
    public void setFood(Food food) { this.food = food; }
    public Integer getQuantityGrams() { return quantityGrams; }
    public void setQuantityGrams(Integer quantityGrams) { this.quantityGrams = quantityGrams; }
}
