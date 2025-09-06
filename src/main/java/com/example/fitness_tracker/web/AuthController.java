package com.example.fitness_tracker.web;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.fitness_tracker.domain.RegistrationForm;
import com.example.fitness_tracker.domain.User;
import com.example.fitness_tracker.repo.RoleRepository;
import com.example.fitness_tracker.repo.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class AuthController {
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepo, RoleRepository roleRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
    }
    
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterForm(org.springframework.ui.Model model) {
        model.addAttribute("form", new RegistrationForm());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("form") RegistrationForm form,
                        HttpServletRequest request) throws ServletException {

        if (userRepo.findByEmail(form.getEmail()).isPresent()) {
            return "redirect:/register?error=email_taken";
        }

        var user = new User();
        user.setEmail(form.getEmail());
        user.setUsername(form.getUsername());
        user.setPassword(passwordEncoder.encode(form.getPassword()));
        user.setEnabled(true);
        user.setRole(roleRepo.findByName("USER").orElseThrow()); // see #2 below
        userRepo.save(user); // committed by the time it returns

        request.login(form.getEmail(), form.getPassword()); // now it can find the user
        return "redirect:/";
    }

}