package com.bookstore;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class AdminBooksController {

    private final BookRepository bookRepository;

    public AdminBooksController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // ==========================================================
    // ADMIN BOOKS PAGE
    // ==========================================================

    @GetMapping("/admin/books")
    public String books(Model model) {

        List<Book> books = bookRepository.findAll();

        // Total books
        long totalBooks = books.size();

        // Books with stock greater than 10
        long inStock = books.stream()
                .filter(book -> book.getStock() > 10)
                .count();

        // Books with stock from 1 to 10
        long lowStock = books.stream()
                .filter(book -> book.getStock() > 0
                        && book.getStock() <= 10)
                .count();

        // Books with zero stock
        long outOfStock = books.stream()
                .filter(book -> book.getStock() == 0)
                .count();

        // Send data to Thymeleaf
        model.addAttribute("books", books);
        model.addAttribute("totalBooks", totalBooks);
        model.addAttribute("inStock", inStock);
        model.addAttribute("lowStock", lowStock);
        model.addAttribute("outOfStock", outOfStock);

        return "admin/books";
    }

    // ==========================================================
    // DELETE BOOK
    // ==========================================================

    @GetMapping("/admin/books/delete/{id}")
    public String deleteBook(@PathVariable Long id) {

        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
        }

        return "redirect:/admin/books";
    }
}