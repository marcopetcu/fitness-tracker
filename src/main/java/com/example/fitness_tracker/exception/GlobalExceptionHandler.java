package com.example.fitness_tracker.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public String handleNotFound(NotFoundException ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        return "errors/404";
    }

    @ExceptionHandler(Exception.class)
    public String handleOther(Exception ex, Model model) {
        model.addAttribute("message", "Internal Error");
        return "errors/500";
    }
}