package com.bookstore;

import com.bookstore.model.Admin;
import com.bookstore.repository.AdminRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminLoginController {

    private final AdminRepository adminRepository;

    public AdminLoginController(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    // ==============================
    // SHOW ADMIN LOGIN PAGE
    // ==============================

    @GetMapping("/admin/login")
    public String adminLogin() {
        return "admin-login";
    }


    // ==============================
    // PROCESS ADMIN LOGIN
    // ==============================

    @PostMapping("/admin/login")
    public String processAdminLogin(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            HttpSession session,
            Model model) {

        Admin admin = adminRepository.findByEmail(email);

        if (admin != null &&
            admin.getPassword().equals(password)) {

            // Store admin login in session
            session.setAttribute("adminLoggedIn", true);
            session.setAttribute("adminEmail", admin.getEmail());
            session.setAttribute("adminUsername", admin.getUsername());

            return "redirect:/admin/dashboard";
        }

        model.addAttribute(
                "error",
                "Invalid admin email or password."
        );

        return "admin-login";
    }
}