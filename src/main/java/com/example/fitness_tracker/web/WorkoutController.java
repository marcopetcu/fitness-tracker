package com.example.fitness_tracker.web;

import com.example.fitness_tracker.domain.User;
import com.example.fitness_tracker.domain.Workout;
import com.example.fitness_tracker.repo.ExerciseRepository;
import com.example.fitness_tracker.service.WorkoutService;
import com.example.fitness_tracker.web.dto.AddExerciseToWorkoutForm;
import com.example.fitness_tracker.web.dto.UpdateExerciseLineForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/workout")
public class WorkoutController {
    private final WorkoutService workoutService;
    private final ExerciseRepository exerciseRepo;
    private final CurrentUserFacade currentUser;

    public WorkoutController(WorkoutService workoutService,
                            ExerciseRepository exerciseRepo,
                            CurrentUserFacade currentUser) {
        this.workoutService = workoutService;
        this.exerciseRepo = exerciseRepo;
        this.currentUser = currentUser;
    }


    @GetMapping
    public String viewWorkout(@RequestParam(required = false)
                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                              @RequestParam(required = false, defaultValue = "") String q,
                              Pageable pageable,
                              @AuthenticationPrincipal UserDetails principal,
                              Model model) {
        User user = currentUser.require(principal);
        LocalDate d = (date != null) ? date : LocalDate.now();

        Workout w = workoutService.getOrCreate(user, d);
        model.addAttribute("workout", w);
        model.addAttribute("date", d);
        model.addAttribute("prevDate", d.minusDays(1));
        model.addAttribute("nextDate", d.plusDays(1));

        // exercise search (like Food list)
        Page<?> exercises = (q == null || q.isBlank())
                ? exerciseRepo.findAll(pageable)
                : exerciseRepo.findByNameContainingIgnoreCase(q, pageable);
        model.addAttribute("exercises", exercises);
        model.addAttribute("q", q);

        model.addAttribute("addForm", new AddExerciseToWorkoutForm());
        return "workout/view";
    }

    @PostMapping("/add")
    public String add(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                      @ModelAttribute("addForm") AddExerciseToWorkoutForm form,
                      BindingResult br,
                      @AuthenticationPrincipal UserDetails principal) {
        if (br.hasErrors()) {
            return "redirect:/workout?date=" + date;
        }
        User user = currentUser.require(principal);
        workoutService.addExercise(user, date, form);
        return "redirect:/workout?date=" + date;
    }

    @PostMapping("/update")
    public String update(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                         @ModelAttribute UpdateExerciseLineForm form,
                         BindingResult br,
                         @AuthenticationPrincipal UserDetails principal) {
        if (br.hasErrors()) {
            return "redirect:/workout?date=" + date;
        }
        User user = currentUser.require(principal);
        workoutService.updateLine(user, date, form);
        return "redirect:/workout?date=" + date;
    }

    @PostMapping("/remove")
    public String remove(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                         @RequestParam Long exerciseId,
                         @AuthenticationPrincipal UserDetails principal) {
        User user = currentUser.require(principal);
        workoutService.removeLine(user, date, exerciseId);
        return "redirect:/workout?date=" + date;
    }
}
