package com.instantops.mechanic.dto;

import com.instantops.mechanic.entity.Mechanic;
import com.instantops.mechanic.entity.MechanicStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Comparator;

import com.instantops.booking.dto.MechanicBookingSummary;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MechanicResponse {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String specialization;
    private MechanicStatus status;
    private String avatarUrl;
    private int activeBookingsCount;
    private int jobsCompleted;
    private MechanicBookingSummary currentBooking;
    private MechanicBookingSummary lastBooking;
    private LocalDateTime createdAt;

    public static MechanicResponse fromEntity(Mechanic mechanic) {
        int activeCount = 0;
        int completedCount = 0;
        MechanicBookingSummary currentBooking = null;
        MechanicBookingSummary lastBooking = null;
        if (mechanic.getBookings() != null) {
            var bookings = mechanic.getBookings();
            activeCount = (int) bookings.stream()
                    .filter(b -> b.getStatus() != com.instantops.booking.entity.BookingStatus.COMPLETED &&
                                 b.getStatus() != com.instantops.booking.entity.BookingStatus.CANCELLED)
                    .count();
            completedCount = (int) bookings.stream()
                    .filter(b -> b.getStatus() == com.instantops.booking.entity.BookingStatus.COMPLETED)
                    .count();
            currentBooking = bookings.stream()
                    .filter(b -> b.getStatus() != com.instantops.booking.entity.BookingStatus.COMPLETED &&
                                 b.getStatus() != com.instantops.booking.entity.BookingStatus.CANCELLED)
                    .min(Comparator.comparing(com.instantops.booking.entity.Booking::getScheduledAt, Comparator.nullsLast(Comparator.naturalOrder())))
                    .map(MechanicBookingSummary::fromEntity)
                    .orElse(null);
            lastBooking = bookings.stream()
                    .filter(b -> b.getStatus() == com.instantops.booking.entity.BookingStatus.COMPLETED ||
                                 b.getStatus() == com.instantops.booking.entity.BookingStatus.CANCELLED)
                    .max(Comparator.comparing(com.instantops.booking.entity.Booking::getScheduledAt, Comparator.nullsLast(Comparator.naturalOrder())))
                    .map(MechanicBookingSummary::fromEntity)
                    .orElse(null);
        }

        return MechanicResponse.builder()
                .id(mechanic.getId())
                .name(mechanic.getName())
                .email(mechanic.getEmail())
                .phone(mechanic.getPhone())
                .specialization(mechanic.getSpecialization())
                .status(mechanic.getStatus())
                .avatarUrl(mechanic.getAvatarUrl())
                .activeBookingsCount(activeCount)
                .jobsCompleted(completedCount)
                .currentBooking(currentBooking)
                .lastBooking(lastBooking)
                .createdAt(mechanic.getCreatedAt())
                .build();
    }
}
