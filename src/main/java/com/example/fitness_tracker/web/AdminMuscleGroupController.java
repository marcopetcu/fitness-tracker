package com.example.fitness_tracker.web;

import com.example.fitness_tracker.domain.MuscleGroup;
import com.example.fitness_tracker.repo.MuscleGroupRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/muscle-groups")
@PreAuthorize("hasRole('ADMIN')")
public class AdminMuscleGroupController {
    private final MuscleGroupRepository repo;
    public AdminMuscleGroupController(MuscleGroupRepository repo) { this.repo = repo; }

    public static class MgForm {
        @NotBlank public String name;
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("items", repo.findAll());
        return "mg/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("form", new MgForm());
        return "mg/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("form") MgForm form) {
        var mg = new MuscleGroup();
        mg.setName(form.name);
        repo.save(mg);
        return "redirect:/admin/muscle-groups";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        var mg = repo.findById(id).orElseThrow();
        var form = new MgForm(); form.setName(mg.getName());
        model.addAttribute("form", form);
        model.addAttribute("id", id);
        return "mg/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("form") MgForm form) {
        var mg = repo.findById(id).orElseThrow();
        mg.setName(form.name);
        repo.save(mg);
        return "redirect:/admin/muscle-groups";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        repo.deleteById(id);
        return "redirect:/admin/muscle-groups";
    }
}