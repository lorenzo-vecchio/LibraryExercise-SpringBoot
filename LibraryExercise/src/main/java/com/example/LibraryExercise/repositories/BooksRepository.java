package com.example.LibraryExercise.repositories;

import com.example.LibraryExercise.models.Book;
import com.example.LibraryExercise.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface BooksRepository extends CrudRepository<Book, Integer> {

    @Query("select b from Book b where title = :title")
    public Book findByTitle(String title);
}
