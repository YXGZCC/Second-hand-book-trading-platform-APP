package com.example.book.ui.home;

public class Book {
    private int bookId;
    private String name;
    private double currentPrice;
    private String author;
    private String username;

    public Book(int bookId, String name, double currentPrice, String author, String username) {
        this.bookId = bookId;
        this.name = name;
        this.currentPrice = currentPrice;
        this.author = author;
        this.username = username;
    }

    public int getBookId() {
        return bookId;
    }

    public String getName() {
        return name;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public String getAuthor() {
        return author;
    }

    public String getUsername() {
        return username;
    }
}
