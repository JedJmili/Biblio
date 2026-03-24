package com.bibliotech.controller;

import com.bibliotech.dto.BorrowingDTO;
import com.bibliotech.dto.CheckoutDTO;
import com.bibliotech.service.BorrowingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrowings")
@RequiredArgsConstructor
public class BorrowingController {

    private final BorrowingService borrowingService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public ResponseEntity<List<BorrowingDTO>> getAllBorrowings() {
        return ResponseEntity.ok(borrowingService.getAllBorrowings());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BorrowingDTO>> getBorrowingsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(borrowingService.getBorrowingsByUser(userId));
    }

    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public ResponseEntity<List<BorrowingDTO>> getActiveBorrowings() {
        return ResponseEntity.ok(borrowingService.getActiveBorrowings());
    }

    @PostMapping("/checkout")
    public ResponseEntity<BorrowingDTO> checkout(@Valid @RequestBody CheckoutDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(borrowingService.checkout(dto));
    }

    @PutMapping("/{id}/return")
    public ResponseEntity<BorrowingDTO> returnBook(@PathVariable Long id) {
        return ResponseEntity.ok(borrowingService.returnBook(id));
    }
}
