package com.instantops.sse.controller;

import com.instantops.sse.service.SseEmitterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
@Tag(name = "Events", description = "Server-Sent Events (SSE) Live Stream")
public class SseController {

    private final SseEmitterService sseEmitterService;

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "Subscribe to real-time operations event stream",
               description = "Establishes a persistent Server-Sent Events (SSE) connection. Emits 'BOOKING_STATUS_CHANGED' events when bookings are updated.")
    public SseEmitter subscribeToEvents() {
        return sseEmitterService.createEmitter();
    }
}
