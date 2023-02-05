package com.am.bookplus.service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "book", schema = "book")
public class EntityBook
{
    @Id
    @Column(length = 13)
    private String isbn;

    @Column(name="title")
    private String title;

    @Column(name="author")
    private String author;

    @Column(name="quantity")
    private int quantity;

    @OneToMany
    @JoinColumn(name = "book_isbn")
    @JsonIgnore
    private Set<EntityRegister> registerOccurrences;

    public EntityBook(){}
    public EntityBook(String isbn, String title, String author, int quantity)
    {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.quantity = quantity;
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

    public Set<EntityRegister> getRegisterOccurrences() {
        return registerOccurrences;
    }
}