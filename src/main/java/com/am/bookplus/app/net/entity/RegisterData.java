package com.am.bookplus.app.net.entity;

import java.util.Date;

public class RegisterData
{
    public String book_isbn;
    public String student_id;
    public Date date;
    public int days;

    public RegisterData(){}
    public RegisterData(String book_isbn, String student_id, Date date, int days)
    {
        this.book_isbn = book_isbn;
        this.student_id = student_id;
        this.date = date;
        this.days = days;
    }
}
