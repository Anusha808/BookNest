package com.bookstore;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
public class ProfileController {

    private final UserRepository userRepository;


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public ProfileController(UserRepository userRepository) {

        this.userRepository = userRepository;
    }


    // =========================================================
    // SHOW PROFILE
    // =========================================================

    @GetMapping("/profile")
    public String showProfile(
            @RequestParam(required = false) String email,
            Model model) {

        User user = null;


        // -----------------------------------------------------
        // FIND USER USING EMAIL
        // -----------------------------------------------------

        if (email != null && !email.trim().isEmpty()) {

            user = userRepository
                    .findByEmail(email.trim())
                    .orElse(null);
        }


        // -----------------------------------------------------
        // FALLBACK
        // -----------------------------------------------------

        if (user == null) {

            user = userRepository
                    .findAll()
                    .stream()
                    .findFirst()
                    .orElse(null);
        }


        // -----------------------------------------------------
        // USER FOUND
        // -----------------------------------------------------

        if (user != null) {

            model.addAttribute(
                    "user",
                    user
            );

            model.addAttribute(
                    "loggedInUserName",
                    user.getName()
            );

            model.addAttribute(
                    "loggedInUserEmail",
                    user.getEmail()
            );

            model.addAttribute(
                    "loggedInUserRole",
                    user.getRole()
            );

            model.addAttribute(
                    "loggedInUserLocation",
                    user.getLocation()
            );

        }

        // -----------------------------------------------------
        // NO USER FOUND
        // -----------------------------------------------------

        else {

            model.addAttribute(
                    "loggedInUserName",
                    "Guest User"
            );

            model.addAttribute(
                    "loggedInUserEmail",
                    "Please login"
            );

            model.addAttribute(
                    "loggedInUserRole",
                    "USER"
            );

            model.addAttribute(
                    "loggedInUserLocation",
                    "Not specified"
            );
        }


        return "profile";
    }


    // =========================================================
    // UPDATE PROFILE
    // =========================================================

    @PostMapping("/profile/update")
    public String updateProfile(

            @RequestParam Long id,

            @RequestParam String name,

            @RequestParam String email,

            @RequestParam(required = false) String location,

            Model model) {


        // -----------------------------------------------------
        // FIND USER
        // -----------------------------------------------------

        User user = userRepository
                .findById(id)
                .orElse(null);


        // -----------------------------------------------------
        // USER NOT FOUND
        // -----------------------------------------------------

        if (user == null) {

            return "redirect:/profile";
        }


        // -----------------------------------------------------
        // VALIDATE NAME
        // -----------------------------------------------------

        if (name == null || name.trim().isEmpty()) {

            loadUserData(
                    model,
                    user,
                    "Name cannot be empty."
            );

            return "profile";
        }


        // -----------------------------------------------------
        // VALIDATE EMAIL
        // -----------------------------------------------------

        if (email == null || email.trim().isEmpty()) {

            loadUserData(
                    model,
                    user,
                    "Email cannot be empty."
            );

            return "profile";
        }


        // -----------------------------------------------------
        // CHECK DUPLICATE EMAIL
        // -----------------------------------------------------

        User existingUser = userRepository
                .findByEmail(email.trim())
                .orElse(null);


        if (existingUser != null
                && !existingUser.getId().equals(user.getId())) {

            loadUserData(
                    model,
                    user,
                    "This email address is already registered."
            );

            return "profile";
        }


        // -----------------------------------------------------
        // UPDATE NAME
        // -----------------------------------------------------

        user.setName(
                name.trim()
        );


        // -----------------------------------------------------
        // UPDATE EMAIL
        // -----------------------------------------------------

        user.setEmail(
                email.trim()
        );


        // -----------------------------------------------------
        // UPDATE LOCATION
        // -----------------------------------------------------

        if (location != null
                && !location.trim().isEmpty()) {

            user.setLocation(
                    location.trim()
            );

        } else {

            user.setLocation(
                    "Not specified"
            );
        }


        // -----------------------------------------------------
        // SAVE USER
        // -----------------------------------------------------

        userRepository.save(user);


        // -----------------------------------------------------
        // REDIRECT TO PROFILE
        // -----------------------------------------------------

        String encodedEmail =
                URLEncoder.encode(
                        user.getEmail(),
                        StandardCharsets.UTF_8
                );


        return "redirect:/profile?email="
                + encodedEmail
                + "&updated=true";
    }


    // =========================================================
    // LOAD USER DATA WHEN ERROR OCCURS
    // =========================================================

    private void loadUserData(
            Model model,
            User user,
            String error) {


        model.addAttribute(
                "user",
                user
        );


        model.addAttribute(
                "loggedInUserName",
                user.getName()
        );


        model.addAttribute(
                "loggedInUserEmail",
                user.getEmail()
        );


        model.addAttribute(
                "loggedInUserRole",
                user.getRole()
        );


        model.addAttribute(
                "loggedInUserLocation",
                user.getLocation()
        );


        model.addAttribute(
                "error",
                error
        );
    }
}