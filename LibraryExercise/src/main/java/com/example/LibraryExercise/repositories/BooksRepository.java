package com.example.LibraryExercise.repositories;

import com.example.LibraryExercise.models.Book;
import org.springframework.data.repository.CrudRepository;

public interface BooksRepository extends CrudRepository<Book, Integer> {
}
