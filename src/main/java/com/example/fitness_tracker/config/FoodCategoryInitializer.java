package com.example.fitness_tracker.config;

import com.example.fitness_tracker.domain.FoodCategory;
import com.example.fitness_tracker.repo.FoodCategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile({"dev","test","prod"})
public class FoodCategoryInitializer implements CommandLineRunner {
    private final FoodCategoryRepository repo;

    public FoodCategoryInitializer(FoodCategoryRepository repo) {
        this.repo = repo;
    }

    @Override
    public void run(String... args) {
        List<String> defaults = List.of(
                "Fruits",
                "Vegetables",
                "Grains",
                "Legumes",
                "Meat",
                "Poultry",
                "Fish & Seafood",
                "Dairy",
                "Nuts & Seeds",
                "Beverages",
                "Sweets & Snacks",
                "Oils & Fats"
        );

        for (String name : defaults) {
            if (!repo.existsByName(name)) {
                FoodCategory fc = new FoodCategory(); // constructor fără arg.
                fc.setName(name);
                repo.save(fc); // tipul se potrivește perfect cu JpaRepository<FoodCategory, Long>
            }
        }
    }
}
