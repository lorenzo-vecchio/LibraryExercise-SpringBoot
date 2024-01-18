package com.example.LibraryExercise.models;

import com.example.LibraryExercise.annotations.ExcludeGSON;
import com.google.gson.annotations.SerializedName;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Date;

@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer ID;
    @NotNull
    @Size(min=1)
    private String title;
    @NotNull
    @Size(min=2)
    private String author;
    @Size(min=10, max=13)
    @Column(unique = true)
    private String isbn;
    @NotNull(message = "You must insert a value")
    private float price;
    @NotNull
    private Integer annoPubblicazione;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @ExcludeGSON
    private User user;

    public Book(String title, String author, String isbn, Float price, Integer annoPubblicazione) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.price = price;
        this.annoPubblicazione = annoPubblicazione;
    }

    public Book() {}

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Integer getAnnoPubblicazione() {
        return annoPubblicazione;
    }

    public void setAnnoPubblicazione(Integer annoPubblicazione) {
        this.annoPubblicazione = annoPubblicazione;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
