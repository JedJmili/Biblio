package com.bibliotech.repository;

import com.bibliotech.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByIsbn(String isbn);
    boolean existsByIsbn(String isbn);
    List<Book> findByAuthorId(Long authorId);
    List<Book> findByCategoryId(Long categoryId);
    List<Book> findByAvailableCopiesGreaterThan(int copies);

    @Query("SELECT b FROM Book b WHERE b.title LIKE %:keyword% OR b.description LIKE %:keyword%")
    List<Book> searchByKeyword(String keyword);
}
