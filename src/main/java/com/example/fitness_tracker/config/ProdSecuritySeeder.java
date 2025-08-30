package com.example.fitness_tracker.config;

import com.example.fitness_tracker.domain.Role;
import com.example.fitness_tracker.domain.User;
import com.example.fitness_tracker.repo.RoleRepository;
import com.example.fitness_tracker.repo.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Profile("prod")
public class ProdSecuritySeeder implements CommandLineRunner {
    private final RoleRepository roleRepo;
    private final UserRepository userRepo;
    private final PasswordEncoder encoder;

    public ProdSecuritySeeder(RoleRepository roleRepo, UserRepository userRepo, PasswordEncoder encoder) {
        this.roleRepo = roleRepo;
        this.userRepo = userRepo;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        Role adminRole = roleRepo.findByName("ROLE_ADMIN").orElseGet(() -> {
            Role r = new Role(); r.setName("ROLE_ADMIN"); return roleRepo.save(r);
        });
        Role userRole = roleRepo.findByName("ROLE_USER").orElseGet(() -> {
            Role r = new Role(); r.setName("ROLE_USER"); return roleRepo.save(r);
        });

        if (userRepo.findByEmail("admin@example.com").isEmpty()) {
            User admin = new User();
            admin.setEmail("admin@example.com");
            admin.setPassword(encoder.encode("admin"));
            admin.setEnabled(true);
            admin.setRoles(Set.of(adminRole, userRole));
            userRepo.save(admin);
        }
        if (userRepo.findByEmail("user@example.com").isEmpty()) {
            User user = new User();
            user.setEmail("user@example.com");
            user.setPassword(encoder.encode("user"));
            user.setEnabled(true);
            user.setRoles(Set.of(userRole));
            userRepo.save(user);
        }
    }
}
