package com.example.fitness_tracker.repo;

import com.example.fitness_tracker.domain.Role;
import com.example.fitness_tracker.domain.User;
import com.example.fitness_tracker.domain.Workout;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


// Test 1: Create a new user, add an workout for a specific date, verify findByUserIdAndDate returns it
// Test 2: Create a new user, add an workout for a specific date, verify findByUserIdAndDate for a different date returns empty
@DataJpaTest
class WorkoutRepositoryTest {

    @Autowired private WorkoutRepository workoutRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;
    private static final Logger log = LoggerFactory.getLogger(WorkoutRepositoryTest.class);

    private User newUserWithRole() {
        Role role = new Role();
        role.setName("USER");
        role = roleRepository.save(role);

        User u = new User();
        u.setUsername("tester");
        u.setEmail("tester@example.com");
        u.setPassword("secret"); // raw ok for test context
        u.setEnabled(true);
        u.setRole(role);
        return userRepository.save(u);
    }

    @Test
    @DisplayName("findByUserIdAndDate returns workout when present")
    void findByUserIdAndDate_found() {
    log.info("[found] Creating user and role");
    User user = newUserWithRole();
    LocalDate date = LocalDate.of(2025, 1, 15);
    log.info("[found] Persisting workout for userId={} date={} ", user.getId(), date);
    Workout w = new Workout();
    w.setUser(user);
    w.setDate(date);
    workoutRepository.save(w);
    log.info("[found] Querying repository");
    Optional<Workout> found = workoutRepository.findByUserIdAndDate(user.getId(), date);
    log.info("[found] Query result present? {}", found.isPresent());
    assertThat(found).isPresent();
    assertThat(found.get().getUser().getId()).isEqualTo(user.getId());
    assertThat(found.get().getDate()).isEqualTo(date);
    log.info("[found] Assertions passed");
    }

    @Test
    @DisplayName("findByUserIdAndDate returns empty when date differs")
    void findByUserIdAndDate_notFound() {
    log.info("[notFound] Creating user and role");
    User user = newUserWithRole();
    LocalDate existingDate = LocalDate.of(2025, 1, 10);
    LocalDate absentDate = LocalDate.of(2025, 1, 11);
    log.info("[notFound] Persisting workout for date={} (will query date={})", existingDate, absentDate);
    Workout w = new Workout();
    w.setUser(user);
    w.setDate(existingDate);
    workoutRepository.save(w);
    log.info("[notFound] Querying repository for missing date");
    Optional<Workout> missing = workoutRepository.findByUserIdAndDate(user.getId(), absentDate);
    log.info("[notFound] Query present? {}", missing.isPresent());
    assertThat(missing).isNotPresent();
    log.info("[notFound] Assertion passed");
    }
}
