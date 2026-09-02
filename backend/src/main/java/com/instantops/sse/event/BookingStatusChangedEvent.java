package com.instantops.sse.event;

import com.instantops.booking.entity.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingStatusChangedEvent {

    private Long bookingId;
    private String bookingNumber;
    private BookingStatus previousStatus;
    private BookingStatus newStatus;
    private String customerName;
    private String customerPhone;
    private String vehicleInfo;
    private String licensePlate;
    private String serviceName;
    private String mechanicName;
    private BigDecimal totalAmount;
    private String notes;

    @Builder.Default
    private String eventType = "BOOKING_STATUS_CHANGED";

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
}
