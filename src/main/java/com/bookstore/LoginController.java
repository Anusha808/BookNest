package com.bookstore;

import jakarta.servlet.http.HttpSession;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginController(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // =========================
    // SHOW LOGIN PAGE
    // =========================

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // =========================
    // PROCESS LOGIN
    // =========================

    @PostMapping("/login")
    public String processLogin(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            HttpSession session,
            Model model) {

        // Find user by email
        User user = userRepository
                .findByEmail(email)
                .orElse(null);

        // =========================
        // USER NOT FOUND
        // =========================

        if (user == null) {

            model.addAttribute(
                    "message",
                    "Invalid email or password."
            );

            return "login";
        }

        // =========================
        // CHECK PASSWORD
        // =========================

        if (!passwordEncoder.matches(
                password,
                user.getPassword())) {

            model.addAttribute(
                    "message",
                    "Invalid email or password."
            );

            return "login";
        }

        // =========================
        // LOGIN SUCCESSFUL
        // CREATE SESSION
        // =========================

        session.setAttribute(
                "loggedInUserId",
                user.getId()
        );

        session.setAttribute(
                "loggedInUserName",
                user.getName()
        );

        session.setAttribute(
                "loggedInUserEmail",
                user.getEmail()
        );

        session.setAttribute(
                "loggedInUserRole",
                user.getRole()
        );

        // =========================
        // REDIRECT TO HOME PAGE
        // =========================

        return "redirect:/home";
    }

    // =========================
    // LOGOUT
    // =========================

    @GetMapping("/logout")
    public String logout(HttpSession session) {

        // Destroy login session
        session.invalidate();

        // Return to login page
        return "redirect:/login?logout=true";
    }
}