package com.bookstore;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BookDetailsController {

    @GetMapping("/book-details")
    public String bookDetails(
            @RequestParam(value = "isbn", required = false) String isbn,
            Model model) {

        model.addAttribute("isbn", isbn);

        return "book-details";
    }
}