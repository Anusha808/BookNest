package com.bookstore;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AdminOrdersController {

    private final OrderRepository orderRepository;

    public AdminOrdersController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // ==========================================================
    // ADMIN ORDERS PAGE
    // ==========================================================

    @GetMapping("/admin/orders")
    public String orders(Model model) {

        List<Order> orders = orderRepository.findAll();

        // ======================================================
        // ORDER STATISTICS
        // ======================================================

        long totalOrders = orders.size();

        long pendingOrders = orders.stream()
                .filter(order ->
                        "PENDING".equalsIgnoreCase(order.getStatus()))
                .count();

        long processingOrders = orders.stream()
                .filter(order ->
                        "PROCESSING".equalsIgnoreCase(order.getStatus()))
                .count();

        long shippedOrders = orders.stream()
                .filter(order ->
                        "SHIPPED".equalsIgnoreCase(order.getStatus()))
                .count();

        long completedOrders = orders.stream()
                .filter(order ->
                        "COMPLETED".equalsIgnoreCase(order.getStatus()))
                .count();

        long cancelledOrders = orders.stream()
                .filter(order ->
                        "CANCELLED".equalsIgnoreCase(order.getStatus()))
                .count();


        // ======================================================
        // PAYMENT STATISTICS
        // ======================================================

        long razorpayOrders = orderRepository
                .countByPaymentStatus("PAID");

        long codOrders = orderRepository
                .countCodOrders();

        long codPending = orderRepository
                .countCodPending();

        long codPaid = orderRepository
                .countCodPaid();


        // ======================================================
        // TOTAL REVENUE
        //
        // Razorpay PAID
        // + COD_PAID
        // ======================================================

        Double totalRevenue =
                orderRepository.getTotalRevenue();

        if (totalRevenue == null) {
            totalRevenue = 0.0;
        }


        // ======================================================
        // SEND DATA TO THYMELEAF
        // ======================================================

        model.addAttribute("orders", orders);

        model.addAttribute("totalOrders", totalOrders);

        model.addAttribute("pendingOrders", pendingOrders);

        model.addAttribute("processingOrders", processingOrders);

        model.addAttribute("shippedOrders", shippedOrders);

        model.addAttribute("completedOrders", completedOrders);

        model.addAttribute("cancelledOrders", cancelledOrders);

        model.addAttribute("razorpayOrders", razorpayOrders);

        model.addAttribute("codOrders", codOrders);

        model.addAttribute("codPending", codPending);

        model.addAttribute("codPaid", codPaid);

        model.addAttribute(
                "totalRevenue",
                String.format("%.2f", totalRevenue)
        );


        return "admin/orders";
    }


    // ==========================================================
    // UPDATE ORDER STATUS
    // ==========================================================

    @PostMapping("/admin/orders/update-status/{id}")
    public String updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        Order order = orderRepository
                .findById(id)
                .orElse(null);

        if (order != null) {

            order.setStatus(status);

            orderRepository.save(order);
        }

        return "redirect:/admin/orders";
    }


    // ==========================================================
    // UPDATE PAYMENT STATUS
    // ==========================================================

    @PostMapping("/admin/orders/update-payment-status/{id}")
    public String updatePaymentStatus(
            @PathVariable Long id,
            @RequestParam String paymentStatus) {

        Order order = orderRepository
                .findById(id)
                .orElse(null);

        if (order != null) {

            order.setPaymentStatus(paymentStatus);

            orderRepository.save(order);
        }

        return "redirect:/admin/orders";
    }


    // ==========================================================
    // DELETE ORDER
    // ==========================================================

    @GetMapping("/admin/orders/delete/{id}")
    public String deleteOrder(@PathVariable Long id) {

        if (orderRepository.existsById(id)) {

            orderRepository.deleteById(id);
        }

        return "redirect:/admin/orders";
    }
}