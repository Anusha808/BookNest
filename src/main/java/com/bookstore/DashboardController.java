package com.bookstore;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboard(
            HttpSession session,
            Model model) {

        // Check whether the user is logged in
        Object userId = session.getAttribute("loggedInUserId");

        // If user is not logged in, go to login page
        if (userId == null) {
            return "redirect:/login";
        }

        // Get logged-in user details from session
        String userName =
                (String) session.getAttribute("loggedInUserName");

        String userEmail =
                (String) session.getAttribute("loggedInUserEmail");

        String userRole =
                (String) session.getAttribute("loggedInUserRole");


        // Send user details to Thymeleaf
        model.addAttribute(
                "userName",
                userName
        );

        model.addAttribute(
                "userEmail",
                userEmail
        );

        model.addAttribute(
                "userRole",
                userRole
        );


        return "dashboard";
    }
}