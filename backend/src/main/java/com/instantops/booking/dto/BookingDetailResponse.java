package com.instantops.booking.dto;

import com.instantops.booking.entity.Booking;
import com.instantops.booking.entity.BookingStatus;
import com.instantops.customer.dto.CustomerResponse;
import com.instantops.mechanic.dto.MechanicResponse;
import com.instantops.service.dto.ServiceOfferingResponse;
import com.instantops.vehicle.dto.VehicleResponse;
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
public class BookingDetailResponse {

    private Long id;
    private String bookingNumber;
    private CustomerResponse customer;
    private VehicleResponse vehicle;
    private ServiceOfferingResponse service;
    private MechanicResponse mechanic;
    private BookingStatus status;
    private BigDecimal totalAmount;
    private LocalDateTime scheduledAt;
    private LocalDateTime completedAt;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static BookingDetailResponse fromEntity(Booking booking) {
        return BookingDetailResponse.builder()
                .id(booking.getId())
                .bookingNumber(booking.getBookingNumber())
                .customer(booking.getCustomer() != null ? CustomerResponse.fromEntity(booking.getCustomer()) : null)
                .vehicle(booking.getVehicle() != null ? VehicleResponse.fromEntity(booking.getVehicle()) : null)
                .service(booking.getServiceOffering() != null ? ServiceOfferingResponse.fromEntity(booking.getServiceOffering()) : null)
                .mechanic(booking.getMechanic() != null ? MechanicResponse.fromEntity(booking.getMechanic()) : null)
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
