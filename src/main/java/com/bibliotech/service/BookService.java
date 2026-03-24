package com.bibliotech.service;

import com.bibliotech.dto.BookCreateDTO;
import com.bibliotech.dto.BookDTO;
import com.bibliotech.entity.Author;
import com.bibliotech.entity.Book;
import com.bibliotech.entity.Category;
import com.bibliotech.exception.AuthorNotFoundException;
import com.bibliotech.exception.BookNotFoundException;
import com.bibliotech.exception.CategoryNotFoundException;
import com.bibliotech.exception.DuplicateIsbnException;
import com.bibliotech.mapper.BookMapper;
import com.bibliotech.repository.AuthorRepository;
import com.bibliotech.repository.BookRepository;
import com.bibliotech.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final BookMapper bookMapper;

    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toDTO)
                .collect(Collectors.toList());
    }

    public BookDTO getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + id));
        return bookMapper.toDTO(book);
    }

    public BookDTO getBookByIsbn(String isbn) {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BookNotFoundException("Book not found with ISBN: " + isbn));
        return bookMapper.toDTO(book);
    }

    public List<BookDTO> searchBooks(String keyword) {
        return bookRepository.searchByKeyword(keyword).stream()
                .map(bookMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<BookDTO> getAvailableBooks() {
        return bookRepository.findByAvailableCopiesGreaterThan(0).stream()
                .map(bookMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public BookDTO createBook(BookCreateDTO dto) {
        if (bookRepository.existsByIsbn(dto.getIsbn())) {
            throw new DuplicateIsbnException("Book with ISBN " + dto.getIsbn() + " already exists");
        }

        Book book = bookMapper.toEntity(dto);

        if (dto.getAuthorId() != null) {
            Author author = authorRepository.findById(dto.getAuthorId())
                    .orElseThrow(() -> new AuthorNotFoundException("Author not found with id: " + dto.getAuthorId()));
            book.setAuthor(author);
        }

        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + dto.getCategoryId()));
            book.setCategory(category);
        }

        Book saved = bookRepository.save(book);
        return bookMapper.toDTO(saved);
    }

    @Transactional
    public BookDTO updateBook(Long id, BookCreateDTO dto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + id));

        if (!book.getIsbn().equals(dto.getIsbn()) && bookRepository.existsByIsbn(dto.getIsbn())) {
            throw new DuplicateIsbnException("Book with ISBN " + dto.getIsbn() + " already exists");
        }

        bookMapper.updateEntityFromDTO(dto, book);

        if (dto.getAuthorId() != null) {
            Author author = authorRepository.findById(dto.getAuthorId())
                    .orElseThrow(() -> new AuthorNotFoundException("Author not found with id: " + dto.getAuthorId()));
            book.setAuthor(author);
        }

        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + dto.getCategoryId()));
            book.setCategory(category);
        }

        return bookMapper.toDTO(bookRepository.save(book));
    }

    @Transactional
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new BookNotFoundException("Book not found with id: " + id);
        }
        bookRepository.deleteById(id);
    }
}
