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
@Profile({"dev","test"})
public class DevSecuritySeeder implements CommandLineRunner {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;

    public DevSecuritySeeder(UserRepository userRepo,
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

        // admin (UN rol)
        userRepo.findByEmail("admin@example.com").orElseGet(() -> {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setEnabled(true);
            admin.setRole(adminRole); // <-- un singur rol
            return userRepo.save(admin);
        });

        // user obișnuit
        userRepo.findByEmail("user@example.com").orElseGet(() -> {
            User u = new User();
            u.setUsername("user");
            u.setEmail("user@example.com");
            u.setPassword(passwordEncoder.encode("user"));
            u.setEnabled(true);
            u.setRole(userRole); // <-- un singur rol
            return userRepo.save(u);
        });
    }
}
