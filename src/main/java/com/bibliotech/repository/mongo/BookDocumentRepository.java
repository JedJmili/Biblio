package com.bibliotech.repository.mongo;

import com.bibliotech.entity.mongo.BookDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface BookDocumentRepository extends ReactiveMongoRepository<BookDocument, String> {
    Mono<BookDocument> findByIsbn(String isbn);
    Flux<BookDocument> findByTitleContainingIgnoreCase(String title);
    Flux<BookDocument> findByCategoryName(String categoryName);
}
