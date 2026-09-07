package com.bookstore;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class CategoryController {

    @GetMapping("/categories")
    public String categories() {
        return "categories";
    }

    @GetMapping("/categories/{category}")
    public String categoryPage(
            @PathVariable String category,
            Model model) {

        String categoryName = formatCategoryName(category);

        model.addAttribute("category", category);
        model.addAttribute("categoryName", categoryName);

        return "category";
    }

    private String formatCategoryName(String category) {

        switch (category.toLowerCase()) {

            case "fiction":
                return "Fiction";

            case "self-help":
                return "Self Help";

            case "business":
                return "Business";

            case "productivity":
                return "Productivity";

            case "classics":
                return "Classics";

            case "science":
                return "Science";

            case "fantasy":
                return "Fantasy";

            case "romance":
                return "Romance";

            case "mystery":
                return "Mystery";

            case "history":
                return "History";

            case "technology":
                return "Technology";

            case "psychology":
                return "Psychology";

            default:
                return "Books";
        }
    }
}