package com.example.fitness_tracker.repo;

import com.example.fitness_tracker.domain.UserDetailsProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailsProfileRepository extends JpaRepository<UserDetailsProfile, Long> {
}
