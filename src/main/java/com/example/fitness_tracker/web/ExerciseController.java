package com.example.fitness_tracker.web;

import com.example.fitness_tracker.domain.Exercise;
import com.example.fitness_tracker.domain.MuscleGroup;
import com.example.fitness_tracker.repo.ExerciseRepository;
import com.example.fitness_tracker.repo.MuscleGroupRepository;
import com.example.fitness_tracker.web.dto.ExerciseForm;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/exercises")
public class ExerciseController {
    private final ExerciseRepository exerciseRepo;
    private final MuscleGroupRepository mgRepo;

    public ExerciseController(ExerciseRepository exerciseRepo, MuscleGroupRepository mgRepo) {
        this.exerciseRepo = exerciseRepo;
        this.mgRepo = mgRepo;
    }

    @GetMapping
    public String list(@RequestParam(required = false, defaultValue = "") String q,
                       Pageable pageable, Model model) {
        Page<Exercise> page = q.isBlank()
                ? exerciseRepo.findAll(pageable)
                : exerciseRepo.findByNameContainingIgnoreCase(q, pageable);
        model.addAttribute("page", page);
        model.addAttribute("q", q);
        return "exercise/list";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("form", new ExerciseForm());
        model.addAttribute("muscleGroups", mgRepo.findAll());
        return "exercise/form";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public String create(@Valid @ModelAttribute("form") ExerciseForm form) {
        Exercise ex = new Exercise();
        ex.setName(form.getName());
        MuscleGroup mg = mgRepo.findById(form.getMuscleGroupId())
                .orElseThrow(() -> new IllegalArgumentException("Muscle group not found"));
        ex.setMuscleGroup(mg);
        exerciseRepo.save(ex);
        return "redirect:/exercises";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Exercise ex = exerciseRepo.findById(id).orElseThrow();
        ExerciseForm form = new ExerciseForm();
        form.setName(ex.getName());
        form.setMuscleGroupId(ex.getMuscleGroup().getId());
        model.addAttribute("form", form);
        model.addAttribute("muscleGroups", mgRepo.findAll());
        model.addAttribute("exerciseId", id);
        return "exercise/form";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("form") ExerciseForm form) {
        Exercise ex = exerciseRepo.findById(id).orElseThrow();
        ex.setName(form.getName());
        MuscleGroup mg = mgRepo.findById(form.getMuscleGroupId()).orElseThrow();
        ex.setMuscleGroup(mg);
        exerciseRepo.save(ex);
        return "redirect:/exercises";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        exerciseRepo.deleteById(id);
        return "redirect:/exercises";
    }
}
