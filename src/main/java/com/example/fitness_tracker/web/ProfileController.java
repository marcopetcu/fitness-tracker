package com.example.fitness_tracker.web;

import com.example.fitness_tracker.domain.User;
import com.example.fitness_tracker.service.ProfileService;
import com.example.fitness_tracker.web.dto.ChangePasswordForm;
import com.example.fitness_tracker.web.dto.ProfileForm;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    private final CurrentUserFacade currentUser;
    private final ProfileService profileService;

    public ProfileController(CurrentUserFacade currentUser, ProfileService profileService) {
        this.currentUser = currentUser;
        this.profileService = profileService;
    }

    @GetMapping
    public String view(@AuthenticationPrincipal UserDetails principal, Model model) {
        User u = currentUser.require(principal);
        ProfileForm form = new ProfileForm();
        form.setUsername(u.getUsername());
        form.setEmail(u.getEmail());
        if (u.getDetails() != null) {
            form.setBirthday(u.getDetails().getBirthday());
            form.setHeight(u.getDetails().getHeight());
            form.setWeight(u.getDetails().getWeight());
        }
        model.addAttribute("form", form);
        model.addAttribute("createdAt", u.getCreatedAt());
        model.addAttribute("passwordForm", new ChangePasswordForm());
        return "profile/view";
    }

    @PostMapping
    public String update(@AuthenticationPrincipal UserDetails principal,
                         @Valid @ModelAttribute("form") ProfileForm form) {
        User u = currentUser.require(principal);
        profileService.updateProfile(u, form);
        return "redirect:/profile?updated";
    }

    @PostMapping("/password")
    public String changePassword(@AuthenticationPrincipal UserDetails principal,
                                 @Valid @ModelAttribute("passwordForm") ChangePasswordForm form) {
        User u = currentUser.require(principal);
        profileService.changePassword(u, form);
        return "redirect:/profile?passwordChanged";
    }
}
