package com.bookstore;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class AdminDashboardController {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final NotificationRepository notificationRepository;

    public AdminDashboardController(
            BookRepository bookRepository,
            UserRepository userRepository,
            OrderRepository orderRepository,
            NotificationRepository notificationRepository) {

        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.notificationRepository = notificationRepository;
    }

    // =========================================================
    // ADMIN DASHBOARD
    // =========================================================

    @GetMapping("/admin/dashboard")
    public String dashboard(Model model) {

        // =====================================================
        // BASIC COUNTS
        // =====================================================

        long totalBooks =
                bookRepository.count();

        long totalUsers =
                userRepository.count();

        long totalOrders =
                orderRepository.count();

        // =====================================================
        // TOTAL REVENUE
        // PAID + COD_PAID
        // =====================================================

        Double totalRevenue =
                orderRepository.getTotalRevenue();

        if (totalRevenue == null) {
            totalRevenue = 0.0;
        }

        // =====================================================
        // RECENT ORDERS
        // =====================================================

        List<Order> recentOrders =
                orderRepository
                        .findTop5ByOrderByOrderDateDesc();

        // =====================================================
        // ORDER STATUS COUNTS
        // =====================================================

        long pendingOrders =
                orderRepository.countByStatus("PENDING");

        long processingOrders =
                orderRepository.countByStatus("PROCESSING");

        long shippedOrders =
                orderRepository.countByStatus("SHIPPED");

        long completedOrders =
                orderRepository.countByStatus("COMPLETED");

        // =====================================================
        // COD COUNTS
        // =====================================================

        long codOrders =
                orderRepository.countCodOrders();

        long codPending =
                orderRepository.countCodPending();

        long codPaid =
                orderRepository.countCodPaid();

        // =====================================================
        // UNREAD NOTIFICATIONS
        // =====================================================

        long unreadNotifications =
                notificationRepository.countByReadFalse();

        // =====================================================
        // RECENT USERS
        // =====================================================

        List<User> recentUsers =
                userRepository.findAll()
                        .stream()
                        .sorted(
                                Comparator.comparing(
                                        User::getId,
                                        Comparator.nullsLast(
                                                Comparator.reverseOrder()
                                        )
                                )
                        )
                        .limit(5)
                        .collect(Collectors.toList());

        // =====================================================
        // BOOK CATEGORY DATA
        // =====================================================

        List<Book> allBooks =
                bookRepository.findAll();

        long totalBookCount =
                allBooks.size();

        List<Map<String, Object>> stockCategories =
                new ArrayList<>();

        if (totalBookCount > 0) {

            Map<String, Long> categoryCounts =
                    allBooks.stream()
                            .collect(
                                    Collectors.groupingBy(
                                            book -> {

                                                if (book.getCategory() == null
                                                        || book.getCategory()
                                                        .trim()
                                                        .isEmpty()) {

                                                    return "Uncategorized";
                                                }

                                                return book.getCategory();
                                            },
                                            Collectors.counting()
                                    )
                            );

            categoryCounts.entrySet()
                    .stream()
                    .sorted(
                            Map.Entry
                                    .<String, Long>
                                    comparingByValue()
                                    .reversed()
                    )
                    .limit(5)
                    .forEach(entry -> {

                        Map<String, Object> category =
                                new HashMap<>();

                        long count =
                                entry.getValue();

                        int percentage =
                                (int) Math.round(
                                        (count * 100.0)
                                                / totalBookCount
                                );

                        category.put(
                                "name",
                                entry.getKey()
                        );

                        category.put(
                                "count",
                                count
                        );

                        category.put(
                                "percentage",
                                percentage
                        );

                        stockCategories.add(
                                category
                        );
                    });
        }

        // =====================================================
        // SEND DATA TO THYMELEAF
        // =====================================================

        model.addAttribute(
                "totalBooks",
                totalBooks
        );

        model.addAttribute(
                "totalUsers",
                totalUsers
        );

        model.addAttribute(
                "totalOrders",
                totalOrders
        );

        model.addAttribute(
                "totalRevenue",
                String.format(
                        Locale.US,
                        "%.2f",
                        totalRevenue
                )
        );

        model.addAttribute(
                "recentOrders",
                recentOrders
        );

        model.addAttribute(
                "pendingOrders",
                pendingOrders
        );

        model.addAttribute(
                "processingOrders",
                processingOrders
        );

        model.addAttribute(
                "shippedOrders",
                shippedOrders
        );

        model.addAttribute(
                "completedOrders",
                completedOrders
        );

        model.addAttribute(
                "codOrders",
                codOrders
        );

        model.addAttribute(
                "codPending",
                codPending
        );

        model.addAttribute(
                "codPaid",
                codPaid
        );

        model.addAttribute(
                "unreadNotifications",
                unreadNotifications
        );

        model.addAttribute(
                "recentUsers",
                recentUsers
        );

        model.addAttribute(
                "stockCategories",
                stockCategories
        );

        // =====================================================
        // CURRENT MONTH
        // =====================================================

        String currentMonth =
                YearMonth.now()
                        .format(
                                DateTimeFormatter.ofPattern(
                                        "MMMM yyyy"
                                )
                        );

        model.addAttribute(
                "currentMonth",
                currentMonth
        );

        return "admin-dashboard";
    }
}