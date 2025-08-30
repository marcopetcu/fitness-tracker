package com.example.fitness_tracker.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "journal_food",
       uniqueConstraints = @UniqueConstraint(columnNames = {"journal_id","food_id"}))
public class JournalFood {
    @EmbeddedId
    private JournalFoodId id = new JournalFoodId();

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @MapsId("journalId")
    @JoinColumn(name = "journal_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_jf_journal"))
    private Journal journal;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @MapsId("foodId")
    @JoinColumn(name = "food_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_jf_food"))
    private Food food;

    @Column(name = "quantity_grams", nullable = false)
    private Integer quantityGrams;

    public JournalFoodId getId() { return id; }
    public Journal getJournal() { return journal; }
    public void setJournal(Journal journal) { this.journal = journal; }
    public Food getFood() { return food; }
    public void setFood(Food food) { this.food = food; }
    public Integer getQuantityGrams() { return quantityGrams; }
    public void setQuantityGrams(Integer quantityGrams) { this.quantityGrams = quantityGrams; }
}
