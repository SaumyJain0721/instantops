package com.instantops.sse.service;

import com.instantops.sse.event.BookingStatusChangedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@Slf4j
public class SseEmitterService {

    // 30 minutes connection timeout
    private static final long SSE_TIMEOUT = 30 * 60 * 1000L;

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public SseEmitter createEmitter() {
        SseEmitter emitter = new SseEmitter(SSE_TIMEOUT);

        emitter.onCompletion(() -> {
            log.debug("SSE connection completed. Removing emitter.");
            emitters.remove(emitter);
        });

        emitter.onTimeout(() -> {
            log.debug("SSE connection timed out. Closing emitter.");
            emitter.complete();
            emitters.remove(emitter);
        });

        emitter.onError(throwable -> {
            log.debug("SSE connection error: {}. Removing emitter.", throwable.getMessage());
            emitters.remove(emitter);
        });

        emitters.add(emitter);
        log.info("New SSE client connected. Active connections: {}", emitters.size());

        // Send initial connection handshake event
        try {
            emitter.send(SseEmitter.event()
                    .name("INIT")
                    .data("Connected to InstantOps live event stream"));
        } catch (IOException e) {
            log.warn("Failed to send INIT handshake to SSE client: {}", e.getMessage());
            emitters.remove(emitter);
        }

        return emitter;
    }

    public void broadcast(BookingStatusChangedEvent event) {
        if (emitters.isEmpty()) {
            log.debug("No active SSE clients connected to receive event for booking {}", event.getBookingNumber());
            return;
        }

        log.info("Broadcasting BOOKING_STATUS_CHANGED event for booking {} ({}) to {} clients",
                event.getBookingNumber(), event.getNewStatus(), emitters.size());

        List<SseEmitter> deadEmitters = new ArrayList<>();

        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event()
                        .name("BOOKING_STATUS_CHANGED")
                        .data(event));
            } catch (Exception e) {
                log.debug("Failed to deliver SSE event to client. Marking emitter for removal.");
                deadEmitters.add(emitter);
            }
        }

        if (!deadEmitters.isEmpty()) {
            emitters.removeAll(deadEmitters);
            log.debug("Cleaned up {} disconnected SSE emitters. Active: {}", deadEmitters.size(), emitters.size());
        }
    }

    @Scheduled(fixedRate = 25000)
    public void sendHeartbeat() {
        if (emitters.isEmpty()) {
            return;
        }

        List<SseEmitter> deadEmitters = new ArrayList<>();
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event()
                        .comment("keep-alive"));
            } catch (Exception e) {
                deadEmitters.add(emitter);
            }
        }

        if (!deadEmitters.isEmpty()) {
            emitters.removeAll(deadEmitters);
            log.debug("Heartbeat removed {} dead SSE emitters. Active: {}", deadEmitters.size(), emitters.size());
        }
    }

    public int getActiveConnectionCount() {
        return emitters.size();
    }
}
