package com.example.fitness_tracker.web;

import com.example.fitness_tracker.domain.User;
import com.example.fitness_tracker.repo.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class CurrentUserFacade {
    private final UserRepository userRepo;

    public CurrentUserFacade(UserRepository userRepo) { this.userRepo = userRepo; }

    public User require(UserDetails principal) {
        // principal.getUsername() == email
        return userRepo.findByEmail(principal.getUsername())
            .orElseThrow(() -> new IllegalStateException("Logged user not found"));
    }
}
