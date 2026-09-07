package com.bookstore;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AdminAddBookController {

    private final BookService bookService;

    public AdminAddBookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/admin/books/add")
    public String addBookPage(Model model) {

        model.addAttribute("book", new Book());

        return "admin/add-book";
    }

    @PostMapping("/admin/books/add")
    public String addBook(@ModelAttribute Book book) {

        bookService.saveBook(book);

        return "redirect:/admin/books";
    }
}