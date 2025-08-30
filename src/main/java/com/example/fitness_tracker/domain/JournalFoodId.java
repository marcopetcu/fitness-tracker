package com.example.fitness_tracker.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class JournalFoodId implements Serializable {
    @Column(name = "journal_id")
    private Long journalId;

    @Column(name = "food_id")
    private Long foodId;

    public JournalFoodId() {}
    public JournalFoodId(Long journalId, Long foodId) {
        this.journalId = journalId; this.foodId = foodId;
    }

    public Long getJournalId() { return journalId; }
    public void setJournalId(Long journalId) { this.journalId = journalId; }
    public Long getFoodId() { return foodId; }
    public void setFoodId(Long foodId) { this.foodId = foodId; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JournalFoodId that)) return false;
        return Objects.equals(journalId, that.journalId) &&
               Objects.equals(foodId, that.foodId);
    }
    @Override public int hashCode() { return Objects.hash(journalId, foodId); }
}
