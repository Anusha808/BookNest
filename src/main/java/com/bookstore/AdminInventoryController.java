package com.bookstore;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class AdminInventoryController {

    private final BookRepository bookRepository;

    public AdminInventoryController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // ==========================================================
    // INVENTORY PAGE
    // ==========================================================

    @GetMapping("/admin/inventory")
    public String inventory(Model model) {

        List<Book> books = bookRepository.findAll();

        // Inventory statistics
        long totalBooks = books.size();

        long inStock = books.stream()
                .filter(book -> book.getStock() > 10)
                .count();

        long lowStock = books.stream()
                .filter(book -> book.getStock() > 0 && book.getStock() <= 10)
                .count();

        long outOfStock = books.stream()
                .filter(book -> book.getStock() == 0)
                .count();

        int totalUnits = books.stream()
                .mapToInt(Book::getStock)
                .sum();

        double inventoryValue = books.stream()
                .mapToDouble(book ->
                        book.getPrice() * book.getStock())
                .sum();

        model.addAttribute("books", books);
        model.addAttribute("totalBooks", totalBooks);
        model.addAttribute("inStock", inStock);
        model.addAttribute("lowStock", lowStock);
        model.addAttribute("outOfStock", outOfStock);
        model.addAttribute("totalUnits", totalUnits);
        model.addAttribute(
                "inventoryValue",
                String.format("%.2f", inventoryValue)
        );

        return "admin/inventory";
    }


    // ==========================================================
    // UPDATE STOCK
    // ==========================================================

    @PostMapping("/admin/inventory/update-stock/{id}")
    public String updateStock(
            @PathVariable Long id,
            @RequestParam int stock) {

        Book book = bookRepository
                .findById(id)
                .orElse(null);

        if (book != null && stock >= 0) {

            book.setStock(stock);

            bookRepository.save(book);
        }

        return "redirect:/admin/inventory";
    }
}