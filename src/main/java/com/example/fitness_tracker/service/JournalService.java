package com.example.fitness_tracker.service;

import com.example.fitness_tracker.domain.*;
import com.example.fitness_tracker.repo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
public class JournalService {
    private final JournalRepository journalRepo;
    private final JournalFoodRepository jfRepo;
    private final FoodRepository foodRepo;
    private final UserRepository userRepo;

    public JournalService(JournalRepository journalRepo,
                          JournalFoodRepository jfRepo,
                          FoodRepository foodRepo,
                          UserRepository userRepo) {
        this.journalRepo = journalRepo;
        this.jfRepo = jfRepo;
        this.foodRepo = foodRepo;
        this.userRepo = userRepo;
    }

    /** login = username sau email; intern lucrăm cu userId */
    public Journal find(String login, LocalDate date) {
        User u = userRepo.findByEmailOrUsername(login, login).orElse(null);
        if (u == null) {
            log.warn("JournalService.find user not found login='{}'", login);
            return null;
        }
        Journal j = journalRepo.findByUserIdAndDate(u.getId(), date).orElse(null);
        log.debug("JournalService.find userId={} date={} found={}", u.getId(), date, j != null);
        return j;
    }

    public void addFood(String login, LocalDate date, Long foodId, int grams) {
        User u = userRepo.findByEmailOrUsername(login, login).orElseThrow();
        log.debug("JournalService.addFood login='{}' userId={} date={} foodId={} grams={}",
                login, u.getId(), date, foodId, grams);

        Journal j = journalRepo.findByUserIdAndDate(u.getId(), date).orElseGet(() -> {
            Journal created = new Journal();
            created.setUser(u);
            created.setDate(date);
            Journal saved = journalRepo.save(created);
            log.info("JournalService.addFood created journal id={} userId={} date={}",
                    saved.getId(), u.getId(), date);
            return saved;
        });

        Food f = foodRepo.findById(foodId).orElseThrow();
        JournalFood existing = jfRepo.findByJournalAndFood(j, f).orElse(null);
        if (existing != null) {
            int old = existing.getQuantityGrams();
            existing.setQuantityGrams(old + grams);
            jfRepo.save(existing);
            log.info("JournalService.addFood updated entry journalId={} foodId={} grams {} -> {}",
                    j.getId(), f.getId(), old, existing.getQuantityGrams());
        } else {
            JournalFood jf = new JournalFood();
            jf.setJournal(j);
            jf.setFood(f);
            jf.setQuantityGrams(grams);
            jfRepo.save(jf);
            log.info("JournalService.addFood added entry journalId={} foodId={} grams={}",
                    j.getId(), f.getId(), grams);
        }
    }

    public void removeFood(String login, LocalDate date, Long foodId) {
        User u = userRepo.findByEmailOrUsername(login, login).orElse(null);
        if (u == null) {
            log.warn("JournalService.removeFood user not found login='{}'", login);
            return;
        }
        Journal j = journalRepo.findByUserIdAndDate(u.getId(), date).orElse(null);
        if (j == null) {
            log.warn("JournalService.removeFood journal not found userId={} date={}", u.getId(), date);
            return;
        }
        Food f = foodRepo.findById(foodId).orElse(null);
        if (f == null) {
            log.warn("JournalService.removeFood food not found id={}", foodId);
            return;
        }
        jfRepo.findByJournalAndFood(j, f).ifPresentOrElse(jf -> {
            jfRepo.delete(jf);
            log.info("JournalService.removeFood removed entry journalId={} foodId={}", j.getId(), f.getId());
        }, () -> log.warn("JournalService.removeFood entry not found journalId={} foodId={}", j.getId(), f.getId()));
    }

    public Totals totals(Journal j) {
        double kcal = 0, fat = 0, carbs = 0, protein = 0;
        if (j != null) {
            for (JournalFood it : j.getFood()) {
                double factor = (it.getQuantityGrams() == null ? 0.0 : it.getQuantityGrams() / 100.0);
                Food f = it.getFood();
                kcal    += (f.getKcal()    == null ? 0 : f.getKcal())    * factor;
                fat     += (f.getFat()     == null ? 0 : f.getFat())     * factor;
                carbs   += (f.getCarbs()   == null ? 0 : f.getCarbs())   * factor;
                protein += (f.getProtein() == null ? 0 : f.getProtein()) * factor;
            }
        }
        Totals t = new Totals(kcal, fat, carbs, protein);
        log.debug("JournalService.totals journalId={} -> kcal={} fat={} carbs={} protein={}",
                (j != null ? j.getId() : null), t.kcal, t.fat, t.carbs, t.protein);
        return t;
    }

    public static class Totals {
        public final int kcal;
        public final double fat, carbs, protein;
        public Totals(double kcal, double fat, double carbs, double protein) {
            this.kcal = (int)Math.round(kcal);
            this.fat = fat; this.carbs = carbs; this.protein = protein;
        }
    }
}