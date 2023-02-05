package com.am.bookplus.service.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "register", schema = "register")
public class EntityRegister
{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="book_isbn")
    private EntityBook book;

    @ManyToOne
    @JoinColumn(name="student_id")
    private EntityStudent student;

    @Column(name="date")
    private Date date;

    @Column(name="days")
    private Integer days;

    public EntityRegister(){}
    public EntityRegister(Integer id, EntityBook book, EntityStudent student, Date date, Integer days)
    {
        this.id = id;
        this.book = book;
        this.student = student;
        this.date = date;
        this.days = days;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setBook(EntityBook book) {
        this.book = book;
    }

    public void setStudent(EntityStudent student) {
        this.student = student;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public Integer getId() {
        return id;
    }

    public EntityBook getBook() {
        return book;
    }

    public EntityStudent getStudent() {
        return student;
    }

    public Date getDate() {
        return date;
    }

    public Integer getDays() {
        return days;
    }
}