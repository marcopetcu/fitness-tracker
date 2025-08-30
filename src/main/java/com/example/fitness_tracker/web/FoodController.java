package com.example.fitness_tracker.web;

import com.example.fitness_tracker.domain.Food;
import com.example.fitness_tracker.domain.FoodCategory;
import com.example.fitness_tracker.repo.FoodCategoryRepository;
import com.example.fitness_tracker.service.FoodService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/foods")
public class FoodController {
    private final FoodService service;
    private final FoodCategoryRepository categories;

    public FoodController(FoodService service, FoodCategoryRepository categories) {
        this.service = service;
        this.categories = categories;
    }

    @GetMapping
    public String list(@RequestParam(required = false) String q,
                       @PageableDefault(size = 10, sort = "name") Pageable pageable,
                       Model model) {
        model.addAttribute("page", service.list(q, pageable));
        model.addAttribute("q", q);
        return "foods/list";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("food", new Food());
        model.addAttribute("categories", allCategories());
        return "foods/form";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("food", service.get(id));
        model.addAttribute("categories", allCategories());
        return "foods/form";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public String create(@Valid @ModelAttribute("food") Food food, BindingResult br, Model model) {
        if (br.hasErrors()) {
            model.addAttribute("categories", allCategories());
            return "foods/form";
        }
        service.save(food);
        return "redirect:/foods";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("food") Food food, BindingResult br, Model model) {
        if (br.hasErrors()) {
            model.addAttribute("categories", allCategories());
            return "foods/form";
        }
        food.setId(id);
        service.save(food);
        return "redirect:/foods";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/foods";
    }

    private List<FoodCategory> allCategories() {
        return categories.findAll();
    }
}