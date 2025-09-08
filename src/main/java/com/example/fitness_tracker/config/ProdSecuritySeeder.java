package com.example.fitness_tracker.config;


import com.example.fitness_tracker.domain.Role;
import com.example.fitness_tracker.domain.User;
import com.example.fitness_tracker.domain.MuscleGroup;
import com.example.fitness_tracker.domain.Exercise;
import com.example.fitness_tracker.domain.FoodCategory;
import com.example.fitness_tracker.domain.Food;

import com.example.fitness_tracker.repo.ExerciseRepository;
import com.example.fitness_tracker.repo.FoodCategoryRepository;
import com.example.fitness_tracker.repo.FoodRepository;
import com.example.fitness_tracker.repo.MuscleGroupRepository;
import com.example.fitness_tracker.repo.RoleRepository;
import com.example.fitness_tracker.repo.UserRepository;


import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;

@Component
@Profile("prod")
public class ProdSecuritySeeder implements CommandLineRunner {


    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final MuscleGroupRepository muscleGroupRepo;
    private final ExerciseRepository exerciseRepo;
    private final FoodCategoryRepository foodCategoryRepo;  
    private final FoodRepository foodRepo;
    private final PasswordEncoder passwordEncoder;

        public ProdSecuritySeeder(UserRepository userRepo,
                                RoleRepository roleRepo,
                                MuscleGroupRepository muscleGroupRepo,
                                ExerciseRepository exerciseRepo,
                                FoodCategoryRepository foodCategoryRepo,
                                FoodRepository foodRepo,
                                PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.muscleGroupRepo = muscleGroupRepo;
        this.exerciseRepo = exerciseRepo;
        this.foodCategoryRepo = foodCategoryRepo;
        this.foodRepo = foodRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // asigur rolurile
        Role adminRole = roleRepo.findByName("ADMIN").orElseGet(() -> {
            Role r = new Role();
            r.setName("ADMIN");
            return roleRepo.save(r);
        });
        Role userRole = roleRepo.findByName("USER").orElseGet(() -> {
            Role r = new Role();
            r.setName("USER");
            return roleRepo.save(r);
        });

        // admin producție
        userRepo.findByEmail("admin@fitness.local").orElseGet(() -> {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@fitness.local");
            admin.setPassword(passwordEncoder.encode("changeMe!")); // schimbă în prod
            admin.setEnabled(true);
            admin.setRole(adminRole);
            return userRepo.save(admin);
        });

        // user producție (opțional)
        userRepo.findByEmail("user@fitness.local").orElseGet(() -> {
            User u = new User();
            u.setUsername("user");
            u.setEmail("user@fitness.local");
            u.setPassword(passwordEncoder.encode("changeMe!"));
            u.setEnabled(true);
            u.setRole(userRole);
            return userRepo.save(u);
        });


        
        // Food Categories
        FoodCategory fruits = foodCategoryRepo.findByName("Fruits").orElseGet(() -> {
            FoodCategory fc = new FoodCategory();
            fc.setName("Fruits");
            return foodCategoryRepo.save(fc);
        });
        FoodCategory vegetables = foodCategoryRepo.findByName("Vegetables").orElseGet(() -> {
            FoodCategory vc = new FoodCategory();
            vc.setName("Vegetables");
            return foodCategoryRepo.save(vc);
        });
        FoodCategory grains = foodCategoryRepo.findByName("Grains").orElseGet(() -> {
            FoodCategory gc = new FoodCategory();
            gc.setName("Grains");
            return foodCategoryRepo.save(gc);
        });
        FoodCategory poultry = foodCategoryRepo.findByName("Poultry").orElseGet(() -> {
            FoodCategory pc = new FoodCategory();
            pc.setName("Poultry");
            return foodCategoryRepo.save(pc);
        });
        FoodCategory fish = foodCategoryRepo.findByName("Fish & Seafood").orElseGet(() -> {
            FoodCategory fc = new FoodCategory();
            fc.setName("Fish & Seafood");
            return foodCategoryRepo.save(fc);
        });
        FoodCategory dairy = foodCategoryRepo.findByName("Dairy").orElseGet(() -> {
            FoodCategory dc = new FoodCategory();
            dc.setName("Dairy");
            return foodCategoryRepo.save(dc);
        });
        FoodCategory nuts = foodCategoryRepo.findByName("Nuts & Seeds").orElseGet(() -> {
            FoodCategory nc = new FoodCategory();
            nc.setName("Nuts & Seeds");
            return foodCategoryRepo.save(nc);
        });

        // Foods
        foodRepo.findByName("Apple").orElseGet(() -> {
            Food f = new Food();
            f.setName("Apple");
            f.setKcal(52);
            f.setProtein(0.3);
            f.setCarbs(14.0);
            f.setFat(0.2);
            f.setCategory(fruits);
            return foodRepo.save(f);
        });
        foodRepo.findByName("Banana").orElseGet(() -> {
            Food f = new Food();
            f.setName("Banana");
            f.setKcal(89);
            f.setProtein(1.1);
            f.setCarbs(23.0);
            f.setFat(0.3);
            f.setCategory(fruits);
            return foodRepo.save(f);
        });
        foodRepo.findByName("Chicken Breast").orElseGet(() -> {
            Food f = new Food();
            f.setName("Chicken Breast");
            f.setKcal(165);
            f.setProtein(31.0);
            f.setCarbs(0.0);
            f.setFat(3.6);
            f.setCategory(poultry);
            return foodRepo.save(f);
        });
        foodRepo.findByName("Broccoli").orElseGet(() -> {
            Food f = new Food();
            f.setName("Broccoli");
            f.setKcal(34);
            f.setProtein(2.8);
            f.setCarbs(7.0);
            f.setFat(0.4);
            f.setCategory(vegetables);
            return foodRepo.save(f);
        });
        foodRepo.findByName("Rice").orElseGet(() -> {
            Food f = new Food();
            f.setName("Rice");
            f.setKcal(130);
            f.setProtein(2.7);
            f.setCarbs(28.0);
            f.setFat(0.3);
            f.setCategory(grains);
            return foodRepo.save(f);
        });
        foodRepo.findByName("Egg").orElseGet(() -> {
            Food f = new Food();
            f.setName("Egg");
            f.setKcal(68);
            f.setProtein(5.5);
            f.setCarbs(0.6);
            f.setFat(4.8);
            f.setCategory(poultry);
            return foodRepo.save(f);
        });
        foodRepo.findByName("Salmon").orElseGet(() -> {
            Food f = new Food();
            f.setName("Salmon");
            f.setKcal(208);
            f.setProtein(20.0);
            f.setCarbs(0.0);
            f.setFat(13.0);
            f.setCategory(fish);
            return foodRepo.save(f);
        });
        foodRepo.findByName("Oats").orElseGet(() -> {
            Food f = new Food();
            f.setName("Oats");
            f.setKcal(389);
            f.setProtein(16.9);
            f.setCarbs(66.3);
            f.setFat(6.9);
            f.setCategory(grains);
            return foodRepo.save(f);
        });
        foodRepo.findByName("Almonds").orElseGet(() -> {
            Food f = new Food();
            f.setName("Almonds");
            f.setKcal(579);
            f.setProtein(21.2);
            f.setCarbs(21.6);
            f.setFat(49.9);
            f.setCategory(nuts);
            return foodRepo.save(f);
        });
        foodRepo.findByName("Greek Yogurt").orElseGet(() -> {
            Food f = new Food();
            f.setName("Greek Yogurt");
            f.setKcal(59);
            f.setProtein(10.0);
            f.setCarbs(3.6);
            f.setFat(0.4);
            f.setCategory(dairy);
            return foodRepo.save(f);
        });


        // Muscle groups
        MuscleGroup chestCategory = muscleGroupRepo.findByName("Chest").orElseGet(() -> {
            MuscleGroup u = new MuscleGroup();
            u.setName("Chest");
            return muscleGroupRepo.save(u);
        });
        MuscleGroup backCategory = muscleGroupRepo.findByName("Back").orElseGet(() -> {
            MuscleGroup u = new MuscleGroup();
            u.setName("Back");
            return muscleGroupRepo.save(u);
        });
        MuscleGroup shoulderCategory = muscleGroupRepo.findByName("Shoulders").orElseGet(() -> {
            MuscleGroup u = new MuscleGroup();
            u.setName("Shoulders");
            return muscleGroupRepo.save(u);
        });

        // Exercises
        exerciseRepo.findByName("Push-Up").orElseGet(() -> {
            Exercise u = new Exercise();
            u.setName("Push-Up");
            u.setMuscleGroup(chestCategory);
            return exerciseRepo.save(u);
        });
        exerciseRepo.findByName("Bench Press").orElseGet(() -> {
            Exercise u = new Exercise();
            u.setName("Bench Press");
            u.setMuscleGroup(chestCategory);
            return exerciseRepo.save(u);
        });
        exerciseRepo.findByName("Incline Dumbbell Press").orElseGet(() -> {
            Exercise u = new Exercise();
            u.setName("Incline Dumbbell Press");
            u.setMuscleGroup(chestCategory);
            return exerciseRepo.save(u);
        });
        exerciseRepo.findByName("Chest Fly").orElseGet(() -> {
            Exercise u = new Exercise();
            u.setName("Chest Fly");
            u.setMuscleGroup(chestCategory);
            return exerciseRepo.save(u);
        });
        exerciseRepo.findByName("Dips").orElseGet(() -> {
            Exercise u = new Exercise();
            u.setName("Dips");
            u.setMuscleGroup(chestCategory);
            return exerciseRepo.save(u);
        });

        exerciseRepo.findByName("Pull-Up").orElseGet(() -> {
            Exercise u = new Exercise();
            u.setName("Pull-Up");
            u.setMuscleGroup(backCategory);
            return exerciseRepo.save(u);
        });
        exerciseRepo.findByName("Deadlift").orElseGet(() -> {
            Exercise u = new Exercise();
            u.setName("Deadlift");
            u.setMuscleGroup(backCategory);
            return exerciseRepo.save(u);
        });
        exerciseRepo.findByName("Barbell Row").orElseGet(() -> {
            Exercise u = new Exercise();
            u.setName("Barbell Row");
            u.setMuscleGroup(backCategory);
            return exerciseRepo.save(u);
        });
        exerciseRepo.findByName("Lat Pulldown").orElseGet(() -> {
            Exercise u = new Exercise();
            u.setName("Lat Pulldown");
            u.setMuscleGroup(backCategory);
            return exerciseRepo.save(u);
        });
        exerciseRepo.findByName("Seated Cable Row").orElseGet(() -> {
            Exercise u = new Exercise();
            u.setName("Seated Cable Row");
            u.setMuscleGroup(backCategory);
            return exerciseRepo.save(u);
        });

        exerciseRepo.findByName("Overhead Press").orElseGet(() -> {
            Exercise u = new Exercise();
            u.setName("Overhead Press");
            u.setMuscleGroup(shoulderCategory);
            return exerciseRepo.save(u);
        });
        exerciseRepo.findByName("Lateral Raise").orElseGet(() -> {
            Exercise u = new Exercise();
            u.setName("Lateral Raise");
            u.setMuscleGroup(shoulderCategory);
            return exerciseRepo.save(u);
        });
        exerciseRepo.findByName("Front Raise").orElseGet(() -> {
            Exercise u = new Exercise();
            u.setName("Front Raise");
            u.setMuscleGroup(shoulderCategory);
            return exerciseRepo.save(u);
        });
        exerciseRepo.findByName("Rear Delt Fly").orElseGet(() -> {
            Exercise u = new Exercise();
            u.setName("Rear Delt Fly");
            u.setMuscleGroup(shoulderCategory);
            return exerciseRepo.save(u);
        });
        exerciseRepo.findByName("Arnold Press").orElseGet(() -> {
            Exercise u = new Exercise();
            u.setName("Arnold Press");
            u.setMuscleGroup(shoulderCategory);
            return exerciseRepo.save(u);
        });
    }
}
