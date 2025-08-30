package com.example.fitness_tracker.config;

import com.example.fitness_tracker.domain.Role;
import com.example.fitness_tracker.domain.User;
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
    private final PasswordEncoder passwordEncoder;

    public ProdSecuritySeeder(UserRepository userRepo,
                              RoleRepository roleRepo,
                              PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
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
    }
}
