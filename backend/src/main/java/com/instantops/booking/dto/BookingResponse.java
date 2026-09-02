package com.instantops.booking.dto;

import com.instantops.booking.entity.Booking;
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
public class BookingResponse {

    private Long id;
    private String bookingNumber;

    // Customer info
    private Long customerId;
    private String customerName;
    private String customerPhone;
    private String customerEmail;

    // Vehicle info
    private Long vehicleId;
    private String vehicleInfo; // e.g. "2023 Hyundai Creta"
    private String licensePlate;

    // Service info
    private Long serviceId;
    private String serviceName;
    private Integer estimatedDurationMinutes;

    // Mechanic info (nullable)
    private Long mechanicId;
    private String mechanicName;
    private String mechanicSpecialization;

    // Status & financial
    private BookingStatus status;
    private BigDecimal totalAmount;

    // Timestamps
    private LocalDateTime scheduledAt;
    private LocalDateTime completedAt;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static BookingResponse fromEntity(Booking booking) {
        String vehicleInfo = "";
        if (booking.getVehicle() != null) {
            vehicleInfo = String.format("%d %s %s",
                    booking.getVehicle().getYear(),
                    booking.getVehicle().getMake(),
                    booking.getVehicle().getModel());
        }

        return BookingResponse.builder()
                .id(booking.getId())
                .bookingNumber(booking.getBookingNumber())
                .customerId(booking.getCustomer() != null ? booking.getCustomer().getId() : null)
                .customerName(booking.getCustomer() != null ? booking.getCustomer().getName() : null)
                .customerPhone(booking.getCustomer() != null ? booking.getCustomer().getPhone() : null)
                .customerEmail(booking.getCustomer() != null ? booking.getCustomer().getEmail() : null)
                .vehicleId(booking.getVehicle() != null ? booking.getVehicle().getId() : null)
                .vehicleInfo(vehicleInfo)
                .licensePlate(booking.getVehicle() != null ? booking.getVehicle().getLicensePlate() : null)
                .serviceId(booking.getServiceOffering() != null ? booking.getServiceOffering().getId() : null)
                .serviceName(booking.getServiceOffering() != null ? booking.getServiceOffering().getName() : null)
                .estimatedDurationMinutes(booking.getServiceOffering() != null ? booking.getServiceOffering().getEstimatedDurationMinutes() : null)
                .mechanicId(booking.getMechanic() != null ? booking.getMechanic().getId() : null)
                .mechanicName(booking.getMechanic() != null ? booking.getMechanic().getName() : null)
                .mechanicSpecialization(booking.getMechanic() != null ? booking.getMechanic().getSpecialization() : null)
                .status(booking.getStatus())
                .totalAmount(booking.getTotalAmount())
                .scheduledAt(booking.getScheduledAt())
                .completedAt(booking.getCompletedAt())
                .notes(booking.getNotes())
                .createdAt(booking.getCreatedAt())
                .updatedAt(booking.getUpdatedAt())
                .build();
    }
}
