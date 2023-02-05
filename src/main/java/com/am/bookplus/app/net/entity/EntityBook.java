package com.am.bookplus.app.net.entity;

import java.io.Serializable;

public class EntityBook implements Serializable
{
    public String isbn;
    public String title;
    public String author;
    public int quantity;

    public EntityBook(String isbn, String title, String author, int quantity)
    {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.quantity = quantity;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
