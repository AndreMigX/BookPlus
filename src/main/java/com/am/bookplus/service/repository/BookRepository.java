package com.am.bookplus.service.repository;

import com.am.bookplus.service.entity.EntityBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<EntityBook, String>
{
    @Query(value="SELECT COUNT(*) FROM Issue I WHERE I.book_isbn = ?1", nativeQuery = true)
    int getNumberOfIssue(String isbn);
}
