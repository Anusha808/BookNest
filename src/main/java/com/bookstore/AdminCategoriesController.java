package com.bookstore;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class AdminCategoriesController {

    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;

    public AdminCategoriesController(
            CategoryRepository categoryRepository,
            BookRepository bookRepository) {

        this.categoryRepository = categoryRepository;
        this.bookRepository = bookRepository;
    }

    @GetMapping("/admin/categories")
    public String categories(Model model) {

        List<Category> categories =
                categoryRepository.findAll();

        long totalCategories =
                categories.size();

        long totalBooks =
                bookRepository.count();

        model.addAttribute("categories", categories);
        model.addAttribute("totalCategories", totalCategories);
        model.addAttribute("totalBooks", totalBooks);

        return "admin/categories";
    }

    @PostMapping("/admin/categories/add")
    public String addCategory(
            @RequestParam String name,
            @RequestParam(required = false) String description) {

        name = name.trim();

        if (!name.isEmpty()
                && !categoryRepository.existsByNameIgnoreCase(name)) {

            Category category = new Category();

            category.setName(name);
            category.setDescription(description);

            categoryRepository.save(category);
        }

        return "redirect:/admin/categories";
    }

    @GetMapping("/admin/categories/delete/{id}")
    public String deleteCategory(@PathVariable Long id) {

        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
        }

        return "redirect:/admin/categories";
    }
}