package com.bookstore;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class AdminUsersController {

    private final UserRepository userRepository;

    public AdminUsersController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/admin/users")
    public String users(Model model) {

        List<User> users = userRepository.findAll();

        long totalUsers = users.size();

        long adminUsers = users.stream()
                .filter(user -> "ADMIN".equalsIgnoreCase(user.getRole()))
                .count();

        long normalUsers = users.stream()
                .filter(user -> !"ADMIN".equalsIgnoreCase(user.getRole()))
                .count();

        model.addAttribute("users", users);
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("adminUsers", adminUsers);
        model.addAttribute("normalUsers", normalUsers);

        return "admin/users";
    }

    @GetMapping("/admin/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {

        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        }

        return "redirect:/admin/users";
    }
}