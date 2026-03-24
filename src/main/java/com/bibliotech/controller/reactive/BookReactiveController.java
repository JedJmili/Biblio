package com.bibliotech.controller.reactive;

import com.bibliotech.entity.mongo.BookDocument;
import com.bibliotech.repository.mongo.BookDocumentRepository;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/reactive/books")
@RequiredArgsConstructor
public class BookReactiveController {

    private final BookDocumentRepository bookDocumentRepository;

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<BookDocument> getAllBooks() {
        return bookDocumentRepository.findAll();
    }

    @GetMapping("/{id}")
    public Mono<BookDocument> getBookById(@PathVariable String id) {
        return bookDocumentRepository.findById(id);
    }

    @GetMapping("/search")
    public Flux<BookDocument> searchByTitle(@RequestParam String title) {
        return bookDocumentRepository.findByTitleContainingIgnoreCase(title);
    }

    @GetMapping("/isbn/{isbn}")
    public Mono<BookDocument> getByIsbn(@PathVariable String isbn) {
        return bookDocumentRepository.findByIsbn(isbn);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public Mono<BookDocument> createBook(@Valid @RequestBody BookDocument document) {
        return bookDocumentRepository.save(document);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<Void> deleteBook(@PathVariable String id) {
        return bookDocumentRepository.deleteById(id);
    }
}
