package com.am.bookplus.service.controller;

import com.am.bookplus.service.entity.EntityBook;
import com.am.bookplus.service.entity.EntityRegister;
import com.am.bookplus.service.entity.EntityStudent;
import com.am.bookplus.service.entity.RegisterData;
import com.am.bookplus.service.Response;
import com.am.bookplus.service.repository.BookRepository;
import com.am.bookplus.service.repository.RegisterRepository;
import com.am.bookplus.service.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping(path = "/register")
@Transactional
public class RegisterController
{
    @Autowired
    private RegisterRepository registerRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private StudentRepository studentRepository;

    @GetMapping(path = "/list")
    public @ResponseBody Response getRegister(@RequestParam int page, @RequestParam int size)
    {
        Pageable limit = PageRequest.of(page,size, Sort.by(Sort.Direction.ASC, "id"));
        return new Response(Response.ResultCode.RESULT_OK, Response.Request.GET_REGISTER, registerRepository.findAll(limit));
    }

    @PostMapping
    public @ResponseBody Response issueBook(@RequestBody RegisterData registerData)
    {
        Optional<EntityBook> book = bookRepository.findById(registerData.getBook_isbn());
        if (book.isEmpty()) 
            return new Response(Response.ResultCode.RESULT_BOOK_NOT_EXISTS, Response.Request.ISSUE_BOOK);

        Optional<EntityStudent> student = studentRepository.findById(registerData.getStudent_id());
        if (student.isEmpty()) 
            return new Response(Response.ResultCode.RESULT_STUDENT_NOT_EXISTS, Response.Request.ISSUE_BOOK);

        if (book.get().getRegisterOccurrences().size() >= book.get().getQuantity()) 
            return new Response(Response.ResultCode.RESULT_BOOK_NOT_AVAILABLE, Response.Request.ISSUE_BOOK);

        EntityRegister entry = new EntityRegister();
        entry.setBook(book.get());
        entry.setStudent(student.get());
        entry.setDate(registerData.getDate());
        entry.setDays(registerData.getDays());

        return new Response(Response.ResultCode.RESULT_OK, Response.Request.ISSUE_BOOK, registerRepository.save(entry));
    }

    @DeleteMapping
    public @ResponseBody Response returnBook(@RequestParam int id)
    {
        if (registerRepository.findById(id).isEmpty())
            return new Response(Response.ResultCode.RESULT_REGISTER_NOT_EXISTS, Response.Request.RETURN_BOOK);

        registerRepository.deleteById(id);
        return new Response(Response.ResultCode.RESULT_OK, Response.Request.RETURN_BOOK);
    }
}
