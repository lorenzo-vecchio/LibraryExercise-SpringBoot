package com.example.LibraryExercise.controllers;

import com.example.LibraryExercise.models.Book;
import com.example.LibraryExercise.models.User;
import com.example.LibraryExercise.repositories.BooksRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/books")
public class BooksController {
    @Autowired
    private BooksRepository booksRepository;

    @GetMapping("/add-book")
    public String getAddBook(Book book, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            return "redirect:/user/login";
        }
        return "addbook";
    }

    @PostMapping("/add-book")
    public String postAddBook(@Valid Book book, BindingResult bindingResult, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            return "redirect:/user/login";
        }
        if (bindingResult.hasErrors()){
            return "addbook";
        }
        book.setUser(user);
        booksRepository.save(book);
        return "bookaddedmessage";
    }

    @GetMapping("/all-books")
    ModelAndView getAllBooks(HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            return new ModelAndView(new RedirectView("/user/login"));
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("allbooks");
        modelAndView.addObject("books", booksRepository.findAll());
        return modelAndView;
    }

    @GetMapping("/my-books")
    ModelAndView getMyBooks(HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            return new ModelAndView(new RedirectView("/user/login"));
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("mybooks");
        Iterable<Book> allBooks = booksRepository.findAll();
        List<Book> userBooks = new ArrayList<>();
        for (Book book : allBooks) {
            if (book.getUser().getID().equals(user.getID())) {
                userBooks.add(book);
            }
        }
        modelAndView.addObject("books", userBooks);
        return modelAndView;
    }

    @GetMapping("/detail/{id}")
    ModelAndView getBookDetail(@PathVariable("id") int id, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            return new ModelAndView(new RedirectView("/user/login"));
        }
        Book book = booksRepository.findById(id).orElse(null);
        if (book == null) {
            return null;
        }
        boolean isCreator = false;
        if (book.getUser().getID().equals(user.getID())) {
            isCreator = true;
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("bookdetail");
        modelAndView.addObject("book", book);
        modelAndView.addObject("isCreator", isCreator);
        return modelAndView;
    }

    @PostMapping("/remove/{id}")
    String postRemoveBook(@PathVariable("id") int id, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            return "/user/login";
        }
        Book book = booksRepository.findById(id).orElse(null);
        if (book == null) {
            return "redirect:books/all-books";
        }
        if (!book.getUser().getID().equals(user.getID())) {
            return "redirect:/books/detail/" + id;
        }
        booksRepository.delete(book);
        return "bookremovedmessage";
    }
}
