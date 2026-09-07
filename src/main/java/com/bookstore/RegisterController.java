package com.bookstore;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegisterController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterController(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String processRegister(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword,
            Model model) {

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            model.addAttribute("message", "Passwords do not match.");
            return "register";
        }

        // Check minimum password length
        if (password.length() < 6) {
            model.addAttribute(
                    "message",
                    "Password must contain at least 6 characters."
            );
            return "register";
        }

        // Check if email already exists
        if (userRepository.findByEmail(email).isPresent()) {
            model.addAttribute(
                    "message",
                    "An account with this email already exists."
            );
            return "register";
        }

        // Create user
        User user = new User();

        user.setName(name);
        user.setEmail(email);

        // Encrypt password using BCrypt
        user.setPassword(passwordEncoder.encode(password));

        // Default role
        user.setRole("USER");

        // Save user in MySQL
        userRepository.save(user);

        // Registration successful
        return "redirect:/login?registered=success";
    }
}