package com.bibliotech.mapper;

import com.bibliotech.dto.BorrowingDTO;
import com.bibliotech.entity.Book;
import com.bibliotech.entity.Borrowing;
import com.bibliotech.entity.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-24T14:04:56+0000",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.18 (Eclipse Adoptium)"
)
@Component
public class BorrowingMapperImpl implements BorrowingMapper {

    @Override
    public BorrowingDTO toDTO(Borrowing borrowing) {
        if ( borrowing == null ) {
            return null;
        }

        BorrowingDTO.BorrowingDTOBuilder borrowingDTO = BorrowingDTO.builder();

        borrowingDTO.userId( borrowingUserId( borrowing ) );
        borrowingDTO.username( borrowingUserUsername( borrowing ) );
        borrowingDTO.bookId( borrowingBookId( borrowing ) );
        borrowingDTO.bookTitle( borrowingBookTitle( borrowing ) );
        borrowingDTO.bookIsbn( borrowingBookIsbn( borrowing ) );
        borrowingDTO.id( borrowing.getId() );
        borrowingDTO.borrowDate( borrowing.getBorrowDate() );
        borrowingDTO.dueDate( borrowing.getDueDate() );
        borrowingDTO.returnDate( borrowing.getReturnDate() );
        borrowingDTO.status( borrowing.getStatus() );
        borrowingDTO.createdAt( borrowing.getCreatedAt() );

        return borrowingDTO.build();
    }

    private Long borrowingUserId(Borrowing borrowing) {
        if ( borrowing == null ) {
            return null;
        }
        User user = borrowing.getUser();
        if ( user == null ) {
            return null;
        }
        Long id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String borrowingUserUsername(Borrowing borrowing) {
        if ( borrowing == null ) {
            return null;
        }
        User user = borrowing.getUser();
        if ( user == null ) {
            return null;
        }
        String username = user.getUsername();
        if ( username == null ) {
            return null;
        }
        return username;
    }

    private Long borrowingBookId(Borrowing borrowing) {
        if ( borrowing == null ) {
            return null;
        }
        Book book = borrowing.getBook();
        if ( book == null ) {
            return null;
        }
        Long id = book.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String borrowingBookTitle(Borrowing borrowing) {
        if ( borrowing == null ) {
            return null;
        }
        Book book = borrowing.getBook();
        if ( book == null ) {
            return null;
        }
        String title = book.getTitle();
        if ( title == null ) {
            return null;
        }
        return title;
    }

    private String borrowingBookIsbn(Borrowing borrowing) {
        if ( borrowing == null ) {
            return null;
        }
        Book book = borrowing.getBook();
        if ( book == null ) {
            return null;
        }
        String isbn = book.getIsbn();
        if ( isbn == null ) {
            return null;
        }
        return isbn;
    }
}
