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
@RequestMapping("/food")
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
        return "food/list";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("food", new Food());
        model.addAttribute("categories", allCategories());
        return "food/form";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("food", service.get(id));
        model.addAttribute("categories", allCategories());
        return "food/form";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public String create(@Valid @ModelAttribute("food") Food food,
                         BindingResult br,
                         Model model) {
        // asigură-te că avem o categorie selectată (important când <select> leagă *{category.id})
        if (food.getCategory() == null || food.getCategory().getId() == null) {
            br.rejectValue("category", "NotNull.food.category", "Category is required");
        }
        if (br.hasErrors()) {
            model.addAttribute("categories", allCategories());
            return "food/form";
        }

        service.save(food);
        return "redirect:/food";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("food") Food food,
                         BindingResult br,
                         Model model) {
        // idem: verificare categorie
        if (food.getCategory() == null || food.getCategory().getId() == null) {
            br.rejectValue("category", "NotNull.food.category", "Category is required");
        }
        if (br.hasErrors()) {
            model.addAttribute("categories", allCategories());
            return "food/form";
        }

        food.setId(id);
        service.save(food);
        return "redirect:/food";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/food";
    }

    private List<FoodCategory> allCategories() {
        return categories.findAll();
    }
}