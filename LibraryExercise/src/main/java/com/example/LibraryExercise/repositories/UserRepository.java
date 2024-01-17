package com.example.LibraryExercise.repositories;

import com.example.LibraryExercise.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Integer> {
    List<User> findByUsername(String username);
    @Query("select s from User s where username = :username and password = :password")
    public User login(String username, String password);
}
