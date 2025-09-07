package com.example.fitness_tracker.domain;

import jakarta.persistence.*;

@Entity
@Table(
    name = "journal_food",
    uniqueConstraints = @UniqueConstraint(columnNames = {"journal_id", "food_id"})
)
public class JournalFood {

        @EmbeddedId
        private JournalFoodId id = new JournalFoodId();

        @ManyToOne(optional = false, fetch = FetchType.LAZY)
        @MapsId("journalId")
        @JoinColumn(
                name = "journal_id",
                nullable = false,
                foreignKey = @ForeignKey(name = "fk_jf_journal")
        )
        private Journal journal;

        @ManyToOne(optional = false, fetch = FetchType.LAZY)
        @MapsId("foodId")
        @JoinColumn(
                name = "food_id",
                nullable = false,
                foreignKey = @ForeignKey(name = "fk_jf_food")
        )
        private Food food;

        @Column(name = "quantity_grams", nullable = false)
        private Integer quantityGrams;

        public JournalFoodId getId() {
                return id;
        }

        public Journal getJournal() {
                return journal;
        }

        public void setJournal(Journal journal) {
                this.journal = journal;
        }

        public Food getFood() {
                return food;
        }

        public void setFood(Food food) {
                this.food = food;
        }

        public Integer getQuantityGrams() {
                return quantityGrams;
        }

        public void setQuantityGrams(Integer quantityGrams) {
                this.quantityGrams = quantityGrams;
        }
        public Double getTotalKcal() {
                if (food == null || quantityGrams == null) {
                        return 0.0;
                }
                double protein = food.getProtein() == null ? 0.0 : food.getProtein();
                double carbs = food.getCarbs() == null ? 0.0 : food.getCarbs();
                double fat = food.getFat() == null ? 0.0 : food.getFat();

                double kcalPer100g = (protein * 4.0) + (carbs * 4.0) + (fat * 9.0);
                double total = kcalPer100g * quantityGrams / 100.0;

                return java.math.BigDecimal.valueOf(total)
                                .setScale(2, java.math.RoundingMode.HALF_UP)
                                .doubleValue();
        }
        public Double getTotalProtein() {
                if (food == null || food.getProtein() == null || quantityGrams == null) {
                        return 0.0;
                }
                double total = (food.getProtein() * quantityGrams) / 100.0;
                return java.math.BigDecimal.valueOf(total)
                        .setScale(2, java.math.RoundingMode.HALF_UP)
                        .doubleValue();
        }
        public Double getTotalCarbs() {
                if (food == null || food.getCarbs() == null || quantityGrams == null) {
                        return 0.0;
                }
                double total = (food.getCarbs() * quantityGrams) / 100.0;
                return java.math.BigDecimal.valueOf(total)
                                .setScale(2, java.math.RoundingMode.HALF_UP)
                                .doubleValue();
        }

        public Double getTotalFat() {
                if (food == null || food.getFat() == null || quantityGrams == null) {
                        return 0.0;
                }
                double total = (food.getFat() * quantityGrams) / 100.0;
                return java.math.BigDecimal.valueOf(total)
                                .setScale(2, java.math.RoundingMode.HALF_UP)
                                .doubleValue();
        }
}
