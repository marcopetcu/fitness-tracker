package com.example.fitness_tracker.web;

import com.example.fitness_tracker.domain.Food;
import com.example.fitness_tracker.domain.Journal;
import com.example.fitness_tracker.service.JournalService;
import com.example.fitness_tracker.repo.FoodRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/journal")
public class JournalController {
    private final JournalService service;
    private final FoodRepository foodRepo;

    public JournalController(JournalService service, FoodRepository foodRepo) {
        this.service = service;
        this.foodRepo = foodRepo;
    }

    @GetMapping
    public String view(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                       Authentication auth, Model model) {
        LocalDate d = (date == null) ? LocalDate.now() : date;
        String username = auth.getName();
        Journal journal = service.find(username, d);
        var totals = service.totals(journal);
        List<Food> foods = foodRepo.findAll();
        model.addAttribute("date", d);
        model.addAttribute("prevDate", d.minusDays(1));
        model.addAttribute("nextDate", d.plusDays(1));
        model.addAttribute("journal", journal);
        model.addAttribute("foods", foods);
        model.addAttribute("totals", totals);
        return "journal/view";
    }

    @PostMapping("/add")
    public String add(@RequestParam Long foodId,
                      @RequestParam int grams,
                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                      Authentication auth) {
        service.addFood(auth.getName(), date, foodId, grams);
        return "redirect:/journal?date=" + date;
    }

    @PostMapping("/remove")
    public String remove(@RequestParam Long foodId,
                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                         Authentication auth) {
        service.removeFood(auth.getName(), date, foodId);
        return "redirect:/journal?date=" + date;
    }
}
