package com.bibliotech.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OverdueScheduler {

    private final BorrowingService borrowingService;

    @Scheduled(cron = "0 0 1 * * *")
    public void checkOverdueBorrowings() {
        log.info("Checking for overdue borrowings...");
        borrowingService.markOverdueBorrowings();
        log.info("Overdue borrowings check completed.");
    }
}
