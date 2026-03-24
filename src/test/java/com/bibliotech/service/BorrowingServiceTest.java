package com.bibliotech.service;

import com.bibliotech.dto.BorrowingDTO;
import com.bibliotech.dto.CheckoutDTO;
import com.bibliotech.entity.*;
import com.bibliotech.exception.BookAlreadyReturnedException;
import com.bibliotech.exception.MaxBorrowingsExceededException;
import com.bibliotech.exception.OutOfStockException;
import com.bibliotech.mapper.BorrowingMapper;
import com.bibliotech.repository.BookRepository;
import com.bibliotech.repository.BorrowingRepository;
import com.bibliotech.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BorrowingServiceTest {

    @Mock
    private BorrowingRepository borrowingRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BorrowingMapper borrowingMapper;

    @InjectMocks
    private BorrowingService borrowingService;

    private User testUser;
    private Book testBook;
    private CheckoutDTO checkoutDTO;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@test.com")
                .password("password")
                .role(Role.ROLE_USER)
                .enabled(true)
                .build();

        testBook = Book.builder()
                .id(1L)
                .title("Test Book")
                .isbn("978-1234567890")
                .totalCopies(3)
                .availableCopies(2)
                .build();

        checkoutDTO = CheckoutDTO.builder()
                .userId(1L)
                .bookId(1L)
                .loanDurationDays(14)
                .build();
    }

    @Test
    void checkout_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(borrowingRepository.countByUserIdAndStatus(1L, BorrowingStatus.ACTIVE)).thenReturn(0L);

        Borrowing savedBorrowing = Borrowing.builder()
                .id(1L)
                .user(testUser)
                .book(testBook)
                .borrowDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(14))
                .status(BorrowingStatus.ACTIVE)
                .build();

        when(borrowingRepository.save(any(Borrowing.class))).thenReturn(savedBorrowing);
        when(borrowingMapper.toDTO(any(Borrowing.class))).thenReturn(new BorrowingDTO());

        BorrowingDTO result = borrowingService.checkout(checkoutDTO);

        assertNotNull(result);
        verify(bookRepository).save(testBook);
        assertEquals(1, testBook.getAvailableCopies());
    }

    @Test
    void checkout_OutOfStock_ThrowsException() {
        testBook.setAvailableCopies(0);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));

        assertThrows(OutOfStockException.class, () -> borrowingService.checkout(checkoutDTO));
    }

    @Test
    void checkout_MaxBorrowingsExceeded_ThrowsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(borrowingRepository.countByUserIdAndStatus(1L, BorrowingStatus.ACTIVE)).thenReturn(5L);

        assertThrows(MaxBorrowingsExceededException.class, () -> borrowingService.checkout(checkoutDTO));
    }

    @Test
    void returnBook_Success() {
        Borrowing borrowing = Borrowing.builder()
                .id(1L)
                .user(testUser)
                .book(testBook)
                .borrowDate(LocalDate.now().minusDays(7))
                .dueDate(LocalDate.now().plusDays(7))
                .status(BorrowingStatus.ACTIVE)
                .build();

        when(borrowingRepository.findById(1L)).thenReturn(Optional.of(borrowing));
        when(borrowingRepository.save(any(Borrowing.class))).thenReturn(borrowing);
        when(borrowingMapper.toDTO(any(Borrowing.class))).thenReturn(new BorrowingDTO());

        BorrowingDTO result = borrowingService.returnBook(1L);

        assertNotNull(result);
        assertEquals(BorrowingStatus.RETURNED, borrowing.getStatus());
        assertNotNull(borrowing.getReturnDate());
        assertEquals(3, testBook.getAvailableCopies());
    }

    @Test
    void returnBook_AlreadyReturned_ThrowsException() {
        Borrowing borrowing = Borrowing.builder()
                .id(1L)
                .user(testUser)
                .book(testBook)
                .borrowDate(LocalDate.now().minusDays(7))
                .dueDate(LocalDate.now().plusDays(7))
                .returnDate(LocalDate.now().minusDays(1))
                .status(BorrowingStatus.RETURNED)
                .build();

        when(borrowingRepository.findById(1L)).thenReturn(Optional.of(borrowing));

        assertThrows(BookAlreadyReturnedException.class, () -> borrowingService.returnBook(1L));
    }
}
