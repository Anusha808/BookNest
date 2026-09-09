package com.bookstore;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AdminPaymentsController {

    private final OrderRepository orderRepository;

    public AdminPaymentsController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @GetMapping("/admin/payments")
    public String payments(Model model) {

        List<Order> orders = orderRepository.findAll();

        // ======================================================
        // PAYMENT STATISTICS
        // ======================================================

        long totalPayments = orders.stream()
                .filter(order -> order.getPaymentStatus() != null)
                .count();

        long razorpayPayments = orders.stream()
                .filter(order ->
                        "RAZORPAY".equalsIgnoreCase(order.getPaymentMethod())
                        && "PAID".equalsIgnoreCase(order.getPaymentStatus()))
                .count();

        long codPayments = orders.stream()
                .filter(order ->
                        "COD".equalsIgnoreCase(order.getPaymentMethod()))
                .count();

        long codPending = orders.stream()
                .filter(order ->
                        "COD".equalsIgnoreCase(order.getPaymentMethod())
                        && "COD_PENDING".equalsIgnoreCase(order.getPaymentStatus()))
                .count();

        long codPaid = orders.stream()
                .filter(order ->
                        "COD".equalsIgnoreCase(order.getPaymentMethod())
                        && "COD_PAID".equalsIgnoreCase(order.getPaymentStatus()))
                .count();

        long failedPayments = orders.stream()
                .filter(order ->
                        "FAILED".equalsIgnoreCase(order.getPaymentStatus()))
                .count();


        // ======================================================
        // RAZORPAY REVENUE
        // ======================================================

        double razorpayRevenue = orders.stream()
                .filter(order ->
                        "RAZORPAY".equalsIgnoreCase(order.getPaymentMethod())
                        && "PAID".equalsIgnoreCase(order.getPaymentStatus()))
                .mapToDouble(order -> order.getTotalAmount() != null
                        ? order.getTotalAmount()
                        : 0.0)
                .sum();


        // ======================================================
        // COD REVENUE
        // ======================================================

        double codRevenue = orders.stream()
                .filter(order ->
                        "COD".equalsIgnoreCase(order.getPaymentMethod())
                        && "COD_PAID".equalsIgnoreCase(order.getPaymentStatus()))
                .mapToDouble(order -> order.getTotalAmount() != null
                        ? order.getTotalAmount()
                        : 0.0)
                .sum();


        // ======================================================
        // TOTAL COLLECTED REVENUE
        // ======================================================

        double totalRevenue =
                razorpayRevenue + codRevenue;


        // ======================================================
        // SEND DATA TO THYMELEAF
        // ======================================================

        model.addAttribute("orders", orders);

        model.addAttribute("totalPayments", totalPayments);

        model.addAttribute("razorpayPayments", razorpayPayments);

        model.addAttribute("codPayments", codPayments);

        model.addAttribute("codPending", codPending);

        model.addAttribute("codPaid", codPaid);

        model.addAttribute("failedPayments", failedPayments);

        model.addAttribute(
                "razorpayRevenue",
                String.format("%.2f", razorpayRevenue)
        );

        model.addAttribute(
                "codRevenue",
                String.format("%.2f", codRevenue)
        );

        model.addAttribute(
                "totalRevenue",
                String.format("%.2f", totalRevenue)
        );


        return "admin/payments";
    }
}