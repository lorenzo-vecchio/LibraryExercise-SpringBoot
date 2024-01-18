package com.example.LibraryExercise.controllers;

import com.example.LibraryExercise.annotations.ExcludeGSON;
import com.example.LibraryExercise.models.Book;
import com.example.LibraryExercise.repositories.BooksRepository;
import com.example.LibraryExercise.repositories.UserRepository;
import com.google.gson.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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

    @GetMapping("/book/title/{title}")
    public String getBookByTitle(@PathVariable("title") String title) {
        Book book = booksRepository.findByTitle(title);
        if (book == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
        }
        String result = gson.toJson(book);
        return result;
    }

    @GetMapping("/book/id/{id}")
    public String getBookById(@PathVariable("id") Integer id) {
        Book book = booksRepository.findById(id).orElse(null);
        if (book == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
        }
        String result = gson.toJson(book);
        return result;
    }

    @PostMapping("/google-sync")
    public String syncWithGoogle() {
        String apiUrl = "https://www.googleapis.com/books/v1/volumes?q=programming";
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                String responseBody = response.toString();
                JsonObject responseBodyJson = gson.fromJson(responseBody, JsonObject.class);
                JsonArray jsonArray = responseBodyJson.getAsJsonArray("items");
                for (int i = 0; i < jsonArray.size(); i++) {
                    JsonObject item = jsonArray.get(i).getAsJsonObject();
                    Book book = new Book(
                            item.getAsJsonObject("volumeInfo").getAsJsonPrimitive("title").getAsString(),
                            item.getAsJsonObject("volumeInfo").getAsJsonArray("authors").get(0).getAsString(),
                            item.getAsJsonPrimitive("id").getAsString(),
                            100.0F,
                            2000
                    );
                    try {
                        booksRepository.save(book);
                    } catch (Exception e) {
                        continue;
                    }
                }
                return "ok";
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "entity not found");
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "entity not found");
        }
    }
}
