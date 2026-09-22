package com.bookstore;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileController {

    @GetMapping("/profile")
    public String profile(Model model, HttpSession session) {

        Object userId =
                session.getAttribute("loggedInUserId");

        Object userName =
                session.getAttribute("loggedInUserName");

        Object userEmail =
                session.getAttribute("loggedInUserEmail");

        Object userRole =
                session.getAttribute("loggedInUserRole");


        if (userId == null) {

            return "redirect:/";

        }


        model.addAttribute(
                "loggedInUserId",
                userId
        );

        model.addAttribute(
                "loggedInUserName",
                userName
        );

        model.addAttribute(
                "loggedInUserEmail",
                userEmail
        );

        model.addAttribute(
                "loggedInUserRole",
                userRole
        );


        return "profile";
    }
}