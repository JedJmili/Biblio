package com.bibliotech.service;

import com.bibliotech.dto.BorrowingDTO;
import com.bibliotech.dto.CheckoutDTO;
import com.bibliotech.entity.Book;
import com.bibliotech.entity.Borrowing;
import com.bibliotech.entity.BorrowingStatus;
import com.bibliotech.entity.User;
import com.bibliotech.exception.BookAlreadyReturnedException;
import com.bibliotech.exception.BookNotFoundException;
import com.bibliotech.exception.BorrowingNotFoundException;
import com.bibliotech.exception.MaxBorrowingsExceededException;
import com.bibliotech.exception.OutOfStockException;
import com.bibliotech.exception.UserNotFoundException;
import com.bibliotech.mapper.BorrowingMapper;
import com.bibliotech.repository.BookRepository;
import com.bibliotech.repository.BorrowingRepository;
import com.bibliotech.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BorrowingService {

    private static final int MAX_ACTIVE_BORROWINGS = 5;

    private final BorrowingRepository borrowingRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final BorrowingMapper borrowingMapper;

    public List<BorrowingDTO> getAllBorrowings() {
        return borrowingRepository.findAll().stream()
                .map(borrowingMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<BorrowingDTO> getBorrowingsByUser(Long userId) {
        return borrowingRepository.findByUserId(userId).stream()
                .map(borrowingMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<BorrowingDTO> getActiveBorrowings() {
        return borrowingRepository.findByStatus(BorrowingStatus.ACTIVE).stream()
                .map(borrowingMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public BorrowingDTO checkout(CheckoutDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + dto.getUserId()));

        Book book = bookRepository.findById(dto.getBookId())
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + dto.getBookId()));

        if (book.getAvailableCopies() <= 0) {
            throw new OutOfStockException("Book '" + book.getTitle() + "' is out of stock");
        }

        long activeBorrowings = borrowingRepository.countByUserIdAndStatus(dto.getUserId(), BorrowingStatus.ACTIVE);
        if (activeBorrowings >= MAX_ACTIVE_BORROWINGS) {
            throw new MaxBorrowingsExceededException("User has reached the maximum number of active borrowings (" + MAX_ACTIVE_BORROWINGS + ")");
        }

        int loanDays = dto.getLoanDurationDays() > 0 ? dto.getLoanDurationDays() : 14;

        Borrowing borrowing = Borrowing.builder()
                .user(user)
                .book(book)
                .borrowDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(loanDays))
                .status(BorrowingStatus.ACTIVE)
                .build();

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        Borrowing saved = borrowingRepository.save(borrowing);
        return borrowingMapper.toDTO(saved);
    }

    @Transactional
    public BorrowingDTO returnBook(Long borrowingId) {
        Borrowing borrowing = borrowingRepository.findById(borrowingId)
                .orElseThrow(() -> new BorrowingNotFoundException("Borrowing not found with id: " + borrowingId));

        if (borrowing.getStatus() == BorrowingStatus.RETURNED) {
            throw new BookAlreadyReturnedException("Book has already been returned for borrowing id: " + borrowingId);
        }

        borrowing.setReturnDate(LocalDate.now());
        borrowing.setStatus(BorrowingStatus.RETURNED);

        Book book = borrowing.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        return borrowingMapper.toDTO(borrowingRepository.save(borrowing));
    }

    @Transactional
    public void markOverdueBorrowings() {
        List<Borrowing> overdue = borrowingRepository.findByDueDateBeforeAndStatus(
                LocalDate.now(), BorrowingStatus.ACTIVE);
        overdue.forEach(b -> b.setStatus(BorrowingStatus.OVERDUE));
        borrowingRepository.saveAll(overdue);
    }
}
