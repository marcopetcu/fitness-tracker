package com.example.fitness_tracker.service;

import com.example.fitness_tracker.domain.User;
import com.example.fitness_tracker.domain.UserDetailsProfile;
import com.example.fitness_tracker.repo.UserDetailsRepository;
import com.example.fitness_tracker.repo.UserRepository;
import com.example.fitness_tracker.web.dto.ChangePasswordForm;
import com.example.fitness_tracker.web.dto.ProfileForm;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProfileService {
    private final UserRepository userRepo;
    private final UserDetailsRepository detailsRepo;
    private final PasswordEncoder encoder;

    public ProfileService(UserRepository userRepo, UserDetailsRepository detailsRepo, PasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.detailsRepo = detailsRepo;
        this.encoder = encoder;
    }

    public void updateProfile(User user, ProfileForm form) {
        user.setUsername(form.getUsername());
        user.setEmail(form.getEmail());
        userRepo.save(user);

        UserDetailsProfile details = user.getDetails();
        if (details == null) {
            details = new UserDetailsProfile();
            details.setUser(user);
        }
        details.setBirthday(form.getBirthday());
        details.setHeight(form.getHeight());
        details.setWeight(form.getWeight());
        detailsRepo.save(details);
    }

    public void changePassword(User user, ChangePasswordForm form) {
        if (!encoder.matches(form.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }
        if (!form.getNewPassword().equals(form.getConfirmNewPassword())) {
            throw new IllegalArgumentException("New password confirmation does not match");
        }
        user.setPassword(encoder.encode(form.getNewPassword()));
        userRepo.save(user);
    }
}
