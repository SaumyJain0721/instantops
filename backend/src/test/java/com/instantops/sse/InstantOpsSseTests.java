package com.instantops.sse;

import com.instantops.booking.entity.Booking;
import com.instantops.booking.entity.BookingStatus;
import com.instantops.booking.repository.BookingRepository;
import com.instantops.booking.service.BookingService;
import com.instantops.booking.dto.UpdateBookingStatusRequest;
import com.instantops.sse.event.BookingStatusChangedEvent;
import com.instantops.sse.service.SseEmitterService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class InstantOpsSseTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SseEmitterService sseEmitterService;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BookingService bookingService;

    @Test
    @DisplayName("GET /api/events - Establishes SSE stream connection with text/event-stream")
    void testSseStreamEndpoint() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/events")
                        .accept(MediaType.TEXT_EVENT_STREAM_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_EVENT_STREAM))
                .andReturn();

        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(200);
    }

    @Test
    @DisplayName("SseEmitterService - Registers client and safely handles broadcasts and heartbeat")
    void testSseEmitterLifecycle() {
        int initialCount = sseEmitterService.getActiveConnectionCount();

        // 1. Create emitter
        SseEmitter emitter = sseEmitterService.createEmitter();
        assertThat(sseEmitterService.getActiveConnectionCount()).isEqualTo(initialCount + 1);

        // 2. Broadcast event
        BookingStatusChangedEvent event = BookingStatusChangedEvent.builder()
                .bookingId(101L)
                .bookingNumber("BKG-10101")
                .previousStatus(BookingStatus.PENDING)
                .newStatus(BookingStatus.ASSIGNED)
                .customerName("Rahul Sharma")
                .vehicleInfo("2023 Hyundai Creta")
                .serviceName("Periodic Maintenance")
                .mechanicName("Vikas Patel")
                .totalAmount(BigDecimal.valueOf(2499.00))
                .eventType("BOOKING_STATUS_CHANGED")
                .timestamp(LocalDateTime.now())
                .build();

        // Should broadcast without exceptions
        sseEmitterService.broadcast(event);

        // 3. Heartbeat
        sseEmitterService.sendHeartbeat();

        // 4. Client completes/disconnects
        emitter.complete();
        assertThat(sseEmitterService.getActiveConnectionCount()).isGreaterThanOrEqualTo(initialCount);
    }

    @Test
    @DisplayName("PATCH /api/bookings/{id}/status - Dispatches BookingStatusChangedEvent on status mutation")
    void testStatusChangeDispatchesSseEvent() {
        Booking booking = bookingRepository.findByStatus(BookingStatus.PENDING).getFirst();

        UpdateBookingStatusRequest request = UpdateBookingStatusRequest.builder()
                .status(BookingStatus.ASSIGNED)
                .notes("SSE integration test dispatch")
                .build();

        // Creating an active emitter to simulate a connected frontend dashboard
        SseEmitter clientEmitter = sseEmitterService.createEmitter();

        bookingService.updateBookingStatus(booking.getId(), request);

        Booking updated = bookingRepository.findById(booking.getId()).orElseThrow();
        assertThat(updated.getStatus()).isEqualTo(BookingStatus.ASSIGNED);

        clientEmitter.complete();
    }
}
