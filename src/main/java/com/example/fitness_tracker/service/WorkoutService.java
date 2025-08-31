package com.example.fitness_tracker.service;

import com.example.fitness_tracker.domain.*;
import com.example.fitness_tracker.repo.ExerciseRepository;
import com.example.fitness_tracker.repo.ExerciseWorkoutRepository;
import com.example.fitness_tracker.repo.WorkoutRepository;
import com.example.fitness_tracker.web.dto.AddExerciseToWorkoutForm;
import com.example.fitness_tracker.web.dto.UpdateExerciseLineForm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
public class WorkoutService {
    private final WorkoutRepository workoutRepo;
    private final ExerciseRepository exerciseRepo;
    private final ExerciseWorkoutRepository ewRepo;

    public WorkoutService(WorkoutRepository workoutRepo, ExerciseRepository exerciseRepo, ExerciseWorkoutRepository ewRepo) {
        this.workoutRepo = workoutRepo;
        this.exerciseRepo = exerciseRepo;
        this.ewRepo = ewRepo;
    }

    public Workout getOrCreate(User user, LocalDate date) {
        return workoutRepo.findByUserIdAndDate(user.getId(), date).orElseGet(() -> {
            Workout w = new Workout();
            w.setUser(user);
            w.setDate(date);
            return workoutRepo.save(w);
        });
    }

    public void addExercise(User user, LocalDate date, AddExerciseToWorkoutForm form) {
        Workout w = getOrCreate(user, date);
        Exercise ex = exerciseRepo.findById(form.getExerciseId())
                .orElseThrow(() -> new IllegalArgumentException("Exercise not found"));

        ExerciseWorkoutId id = new ExerciseWorkoutId(w.getId(), ex.getId());
        ExerciseWorkout line = ewRepo.findById(id).orElseGet(() -> {
            ExerciseWorkout l = new ExerciseWorkout();
            l.setId(id);
            l.setWorkout(w);
            l.setExercise(ex);
            return l;
        });
        line.setSets(form.getSets());
        line.setReps(form.getReps());
        line.setWeight(form.getWeight());
        ewRepo.save(line);
    }

    public void updateLine(User user, LocalDate date, UpdateExerciseLineForm form) {
        Workout w = workoutRepo.findByUserIdAndDate(user.getId(), date)
                .orElseThrow(() -> new IllegalArgumentException("Workout not found"));
        ExerciseWorkoutId id = new ExerciseWorkoutId(w.getId(), form.getExerciseId());
        ExerciseWorkout line = ewRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Line not found"));
        line.setSets(form.getSets());
        line.setReps(form.getReps());
        line.setWeight(form.getWeight());
        ewRepo.save(line);
    }

    public void removeLine(User user, LocalDate date, Long exerciseId) {
        Workout w = workoutRepo.findByUserIdAndDate(user.getId(), date)
                .orElseThrow(() -> new IllegalArgumentException("Workout not found"));
        ExerciseWorkoutId id = new ExerciseWorkoutId(w.getId(), exerciseId);
        ewRepo.deleteById(id);
    }
}
