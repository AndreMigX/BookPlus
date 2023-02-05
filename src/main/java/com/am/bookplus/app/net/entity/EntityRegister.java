package com.am.bookplus.app.net.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EntityRegister
{
    public int id;
    public String book_isbn;
    public String student_id;
    public Date date;
    public int days;

    public EntityRegister(int id, String book_isbn, String student_id, Date date, int days)
    {
        this.id = id;
        this.book_isbn = book_isbn;
        this.student_id = student_id;
        this.date = date;
        this.days = days;
    }
    public EntityRegister(int id, String book_isbn, String student_id, String date, int days) throws ParseException
    {
        this.id = id;
        this.book_isbn = book_isbn;
        this.student_id = student_id;
        this.date = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        this.days = days;
    }

    public int getId() {
        return id;
    }

    public String getBook_isbn() {
        return book_isbn;
    }

    public String getStudent_id() {
        return student_id;
    }

    public Date getDate() {
        return date;
    }

    public String getStringDate()
    {
        return new SimpleDateFormat("dd/MM/yyyy").format(date);
    }

    public int getDays() {
        return days;
    }
}
