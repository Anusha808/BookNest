package com.bookstore;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class HomeController {

    private final BookRepository bookRepository;

    public HomeController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping("/home")
    public String home(Model model, Principal principal) {

        model.addAttribute("books", bookRepository.findAll());

        if (principal != null) {
            model.addAttribute("loggedInEmail", principal.getName());
        } else {
            model.addAttribute("loggedInEmail", "");
        }

        return "home";
    }
}