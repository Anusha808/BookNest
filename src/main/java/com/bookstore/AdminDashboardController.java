package com.bookstore;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AdminDashboardController {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public AdminDashboardController(
            BookRepository bookRepository,
            UserRepository userRepository,
            OrderRepository orderRepository) {

        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    @GetMapping("/admin/dashboard")
    public String dashboard(Model model) {

        // ==============================
        // BASIC STATISTICS
        // ==============================

        long totalBooks = bookRepository.count();

        long totalUsers = userRepository.count();

        long totalOrders = orderRepository.count();

        Double totalRevenue = orderRepository.getTotalRevenue();

        if (totalRevenue == null) {
            totalRevenue = 0.0;
        }


        // ==============================
        // RECENT ORDERS
        // ==============================

        List<Order> recentOrders =
                orderRepository.findTop5ByOrderByOrderDateDesc();


        // ==============================
        // ORDER STATUS
        // ==============================

        long pendingOrders =
                orderRepository.countByStatus("PENDING");

        long processingOrders =
                orderRepository.countByStatus("PROCESSING");

        long shippedOrders =
                orderRepository.countByStatus("SHIPPED");

        long completedOrders =
                orderRepository.countByStatus("COMPLETED");


        // ==============================
        // SEND DATA TO THYMELEAF
        // ==============================

        model.addAttribute("totalBooks", totalBooks);

        model.addAttribute("totalUsers", totalUsers);

        model.addAttribute("totalOrders", totalOrders);

        model.addAttribute("totalRevenue",
                String.format("%.2f", totalRevenue));

        model.addAttribute("recentOrders", recentOrders);

        model.addAttribute("pendingOrders", pendingOrders);

        model.addAttribute("processingOrders", processingOrders);

        model.addAttribute("shippedOrders", shippedOrders);

        model.addAttribute("completedOrders", completedOrders);


        // ==============================
        // CURRENT MONTH
        // ==============================

        model.addAttribute(
                "currentMonth",
                "September 2026"
        );


        return "admin-dashboard";
    }
}