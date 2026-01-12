package com.example.springbootapp.web.controller;

import com.example.springbootapp.core.dto.RegistrationRequest;
import com.example.springbootapp.core.service.UserService;
import com.example.springbootapp.web.dto.RegistrationForm;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("registrationForm", new RegistrationForm());
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerSubmit(@Valid @ModelAttribute("registrationForm") RegistrationForm form,
                                 BindingResult bindingResult,
                                 Model model) {
        if (!form.getPassword().equals(form.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "password.mismatch", "Passwords do not match");
        }

        if (bindingResult.hasErrors()) {
            return "auth/register";
        }

        try {
            RegistrationRequest request = new RegistrationRequest(form.getEmail(), form.getPassword());
            userService.registerUser(request);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("registrationError", ex.getMessage());
            return "auth/register";
        } catch (DataAccessException ex) {
            log.error("Registration failed for email {}", form.getEmail(), ex);
            model.addAttribute("registrationError", "Registration is unavailable. Please check database configuration.");
            return "auth/register";
        }

        return "redirect:/auth/login?registered";
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }
}
