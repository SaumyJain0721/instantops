package com.instantops.sse.listener;

import com.instantops.sse.event.BookingStatusChangedEvent;
import com.instantops.sse.service.SseEmitterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookingEventListener {

    private final SseEmitterService sseEmitterService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void handleBookingStatusChanged(BookingStatusChangedEvent event) {
        log.debug("Received BookingStatusChangedEvent after transaction commit: {}", event.getBookingNumber());
        sseEmitterService.broadcast(event);
    }
}
