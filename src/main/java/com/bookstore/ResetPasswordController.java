package com.bookstore;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
public class ResetPasswordController {

    private final PasswordResetTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ResetPasswordController(
            PasswordResetTokenRepository tokenRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/reset-password")
    public String showResetPasswordPage(
            @RequestParam("token") String token,
            Model model) {

        PasswordResetToken resetToken =
                tokenRepository.findByToken(token).orElse(null);

        if (resetToken == null ||
                resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {

            model.addAttribute("validToken", false);
            model.addAttribute(
                    "error",
                    "This password reset link is invalid or has expired."
            );

            return "reset-password";
        }

        model.addAttribute("validToken", true);
        model.addAttribute("token", token);

        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(
            @RequestParam("token") String token,
            @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword,
            Model model) {

        PasswordResetToken resetToken =
                tokenRepository.findByToken(token).orElse(null);

        if (resetToken == null ||
                resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {

            model.addAttribute("validToken", false);
            model.addAttribute(
                    "error",
                    "This password reset link is invalid or has expired."
            );

            return "reset-password";
        }

        if (!password.equals(confirmPassword)) {

            model.addAttribute("validToken", true);
            model.addAttribute("token", token);
            model.addAttribute(
                    "error",
                    "Passwords do not match."
            );

            return "reset-password";
        }

        if (password.length() < 6) {

            model.addAttribute("validToken", true);
            model.addAttribute("token", token);
            model.addAttribute(
                    "error",
                    "Password must contain at least 6 characters."
            );

            return "reset-password";
        }

        User user = resetToken.getUser();

        user.setPassword(passwordEncoder.encode(password));

        userRepository.save(user);

        tokenRepository.delete(resetToken);

        return "redirect:/login?reset=success";
    }
}