package com.bookstore;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminCategoryController {

    @GetMapping("/admin/categories")
    public String categories() {
        return "admin/categories";
    }
}