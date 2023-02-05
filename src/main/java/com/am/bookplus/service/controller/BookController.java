package com.am.bookplus.service.controller;

import com.am.bookplus.service.entity.EntityBook;
import com.am.bookplus.service.Response;
import com.am.bookplus.service.Utils;
import com.am.bookplus.service.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping(path = "/book")
@Transactional
public class BookController
{
    @Autowired
    private BookRepository bookRepository;

    @GetMapping(path = "/list")
    public @ResponseBody Response getBooks(@RequestParam int page, @RequestParam int size)
    {
        Pageable limit = PageRequest.of(page,size,Sort.by(Sort.Direction.ASC, "isbn"));
        return new Response(Response.ResultCode.RESULT_OK, Response.Request.GET_BOOKS, bookRepository.findAll(limit));
    }
    
    @GetMapping
    public @ResponseBody Response findBook(@RequestParam String isbn)
    {
        Optional<EntityBook> optionalEntityBook = bookRepository.findById(isbn);
        if (optionalEntityBook.isEmpty())
            return new Response(Response.ResultCode.RESULT_BOOK_NOT_EXISTS, Response.Request.FIND_BOOK);
        
        EntityBook book = optionalEntityBook.get();
        return new Response(Response.ResultCode.RESULT_OK, Response.Request.FIND_BOOK, book);
    }

    @PostMapping
    public @ResponseBody Response addBook(@RequestBody EntityBook book)
    {
        if (bookRepository.findById(book.getIsbn()).isPresent())
            return new Response(Response.ResultCode.RESULT_BOOK_ALREADY_EXISTS, Response.Request.ADD_BOOK);
        
        if (!Utils.checkISBN(book.getIsbn()))
            return new Response(Response.ResultCode.RESULT_ISBN_NOT_VALID, Response.Request.ADD_BOOK);
        
        bookRepository.save(book);
        return new Response(Response.ResultCode.RESULT_OK, Response.Request.ADD_BOOK);
    }

    @DeleteMapping
    public @ResponseBody Response deleteBook(@RequestParam String isbn)
    {
        Optional<EntityBook> optionalEntityBook = bookRepository.findById(isbn);
        if (optionalEntityBook.isEmpty())
            return new Response(Response.ResultCode.RESULT_BOOK_NOT_EXISTS, Response.Request.DELETE_BOOK);
        
        EntityBook book = optionalEntityBook.get();
        if (!book.getRegisterOccurrences().isEmpty())
            return new Response(Response.ResultCode.RESULT_CANT_DELETE_BOOK, Response.Request.DELETE_BOOK);

        bookRepository.deleteById(isbn);
        return new Response(Response.ResultCode.RESULT_OK, Response.Request.DELETE_BOOK);
    }

    @PostMapping(path = "/update")
    public @ResponseBody Response updateBook(@RequestBody EntityBook update)
    {
        Optional<EntityBook> optionalEntityBook = bookRepository.findById(update.getIsbn());
        if (optionalEntityBook.isEmpty())
            return new Response(Response.ResultCode.RESULT_BOOK_NOT_EXISTS, Response.Request.UPDATE_BOOK);

        EntityBook book = optionalEntityBook.get();
        if (book.getRegisterOccurrences().size() > update.getQuantity())
            return new Response(Response.ResultCode.RESULT_QUANTITY_NOT_VALID, Response.Request.UPDATE_BOOK);

        book.setTitle(update.getTitle());
        book.setAuthor(update.getAuthor());
        book.setQuantity(update.getQuantity());

        bookRepository.save(book);
        return new Response(Response.ResultCode.RESULT_OK, Response.Request.UPDATE_BOOK);
    }

    @GetMapping(path = "/check")
    public @ResponseBody Response getAvailability(@RequestParam String isbn)
    {
        Optional<EntityBook> optionalEntityBook = bookRepository.findById(isbn);
        if (optionalEntityBook.isEmpty())
            return new Response(Response.ResultCode.RESULT_BOOK_NOT_EXISTS, Response.Request.CHECK_BOOK);

        EntityBook book = optionalEntityBook.get();

        return new Response(Response.ResultCode.RESULT_OK, Response.Request.CHECK_BOOK, Integer.toString(book.getQuantity() - book.getRegisterOccurrences().size()));
    }
}
