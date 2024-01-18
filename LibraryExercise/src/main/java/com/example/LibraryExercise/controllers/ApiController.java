package com.example.LibraryExercise.controllers;

import com.example.LibraryExercise.annotations.ExcludeGSON;
import com.example.LibraryExercise.repositories.BooksRepository;
import com.example.LibraryExercise.repositories.UserRepository;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {
    @Autowired
    private BooksRepository booksRepository;

    @Autowired
    private UserRepository userRepository;

    ExclusionStrategy strategy = new ExclusionStrategy() {
        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }

        @Override
        public boolean shouldSkipField(FieldAttributes field) {
            return field.getAnnotation(ExcludeGSON.class) != null;
        }
    };

    Gson gson = new GsonBuilder()
            .addSerializationExclusionStrategy(strategy)
            .create();

    @GetMapping("all-books")
    public String getAllBooks() {
        String result = gson.toJson(booksRepository.findAll());
        return result;
    }
}
