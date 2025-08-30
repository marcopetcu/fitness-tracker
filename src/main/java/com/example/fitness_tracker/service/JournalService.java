package com.example.fitness_tracker.service;

import com.example.fitness_tracker.domain.*;
import com.example.fitness_tracker.repo.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class JournalService {
    private final JournalRepository journalRepo;
    private final JournalFoodRepository jfRepo;
    private final FoodRepository foodRepo;

    public JournalService(JournalRepository journalRepo, JournalFoodRepository jfRepo, FoodRepository foodRepo) {
        this.journalRepo = journalRepo;
        this.jfRepo = jfRepo;
        this.foodRepo = foodRepo;
    }

    public Journal find(String username, LocalDate date) {
        return journalRepo.findByUsernameAndDate(username, date).orElse(null);
    }

    public void addFood(String username, LocalDate date, Long foodId, int grams) {
        Journal j = journalRepo.findByUsernameAndDate(username, date).orElseGet(() -> {
            Journal created = new Journal();
            created.setUsername(username);
            created.setDate(date);
            return journalRepo.save(created);
        });
        Food f = foodRepo.findById(foodId).orElseThrow();
        JournalFood existing = jfRepo.findByJournalAndFood(j, f).orElse(null);
        if (existing != null) {
            existing.setQuantityGrams(existing.getQuantityGrams() + grams);
            jfRepo.save(existing);
        } else {
            JournalFood jf = new JournalFood();
            jf.setJournal(j);
            jf.setFood(f);
            jf.setQuantityGrams(grams);
            jfRepo.save(jf);
        }
    }

    public void removeFood(String username, LocalDate date, Long foodId) {
        Journal j = journalRepo.findByUsernameAndDate(username, date).orElse(null);
        if (j == null) return;
        Food f = foodRepo.findById(foodId).orElse(null);
        if (f == null) return;
        jfRepo.findByJournalAndFood(j, f).ifPresent(jfRepo::delete);
    }

    public Totals totals(Journal j) {
        double kcal = 0, fat = 0, carbs = 0, protein = 0;
        if (j != null) {
            for (JournalFood it : j.getFoods()) {
                double factor = (it.getQuantityGrams() == null ? 0.0 : it.getQuantityGrams() / 100.0);
                Food f = it.getFood();
                kcal    += (f.getKcal()    == null ? 0 : f.getKcal())    * factor;
                fat     += (f.getFat()     == null ? 0 : f.getFat())     * factor;
                carbs   += (f.getCarbs()   == null ? 0 : f.getCarbs())   * factor;
                protein += (f.getProtein() == null ? 0 : f.getProtein()) * factor;
            }
        }
        return new Totals(kcal, fat, carbs, protein);
    }

    public static class Totals {
        public final int kcal;
        public final double fat, carbs, protein;
        public Totals(double kcal, double fat, double carbs, double protein) {
            this.kcal = (int) Math.round(kcal);
            this.fat = Math.round(fat * 10) / 10.0;
            this.carbs = Math.round(carbs * 10) / 10.0;
            this.protein = Math.round(protein * 10) / 10.0;
        }
        public int getKcal() { return kcal; }
        public double getFat() { return fat; }
        public double getCarbs() { return carbs; }
        public double getProtein() { return protein; }
    }
}
