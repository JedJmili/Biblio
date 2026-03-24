package com.bibliotech.mapper;

import com.bibliotech.dto.BookCreateDTO;
import com.bibliotech.dto.BookDTO;
import com.bibliotech.entity.Book;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-24T14:04:56+0000",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.18 (Eclipse Adoptium)"
)
@Component
public class BookMapperImpl implements BookMapper {

    @Override
    public BookDTO toDTO(Book book) {
        if ( book == null ) {
            return null;
        }

        BookDTO.BookDTOBuilder bookDTO = BookDTO.builder();

        bookDTO.id( book.getId() );
        bookDTO.title( book.getTitle() );
        bookDTO.isbn( book.getIsbn() );
        bookDTO.description( book.getDescription() );
        bookDTO.publicationYear( book.getPublicationYear() );
        bookDTO.totalCopies( book.getTotalCopies() );
        bookDTO.availableCopies( book.getAvailableCopies() );
        bookDTO.createdAt( book.getCreatedAt() );
        bookDTO.updatedAt( book.getUpdatedAt() );

        bookDTO.authorName( book.getAuthor() != null ? book.getAuthor().getFirstName() + " " + book.getAuthor().getLastName() : null );
        bookDTO.categoryName( book.getCategory() != null ? book.getCategory().getName() : null );

        return bookDTO.build();
    }

    @Override
    public Book toEntity(BookCreateDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Book.BookBuilder book = Book.builder();

        book.availableCopies( dto.getTotalCopies() );
        book.title( dto.getTitle() );
        book.isbn( dto.getIsbn() );
        book.totalCopies( dto.getTotalCopies() );
        book.description( dto.getDescription() );
        book.publicationYear( dto.getPublicationYear() );

        return book.build();
    }

    @Override
    public void updateEntityFromDTO(BookCreateDTO dto, Book book) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getTitle() != null ) {
            book.setTitle( dto.getTitle() );
        }
        if ( dto.getIsbn() != null ) {
            book.setIsbn( dto.getIsbn() );
        }
        book.setTotalCopies( dto.getTotalCopies() );
        if ( dto.getDescription() != null ) {
            book.setDescription( dto.getDescription() );
        }
        book.setPublicationYear( dto.getPublicationYear() );
    }
}
