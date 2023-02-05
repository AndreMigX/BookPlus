package com.am.bookplus.service.entity;

import java.util.Date;

public class RegisterData
{
    private String book_isbn;
    private String student_id;
    private Date date;
    private int days;

    public RegisterData(){}
    public RegisterData(String book_isbn, String student_id, Date date, int days)
    {
        this.book_isbn = book_isbn;
        this.student_id = student_id;
        this.date = date;
        this.days = days;
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

    public int getDays() {
        return days;
    }
}
