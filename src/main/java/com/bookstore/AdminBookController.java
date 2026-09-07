package com.bookstore;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminBookController {

    @GetMapping("/admin/books")
    public String books() {
        return "admin/books";
    }
}