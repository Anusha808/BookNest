package com.bookstore;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class OrdersController {

    private final OrderRepository orderRepository;

    public OrdersController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @GetMapping("/orders")
    public String orders(Model model) {

        List<Order> orders = orderRepository.findAll();

        model.addAttribute("orders", orders);

        return "orders";
    }
}