package com.instantops.booking.dto;

import com.instantops.booking.entity.Booking;
import com.instantops.booking.entity.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MechanicBookingSummary {
    private Long id;
    private String bookingNumber;
    private String customerName;
    private String vehicleInfo;
    private String serviceName;
    private BookingStatus status;
    private LocalDateTime scheduledAt;

    public static MechanicBookingSummary fromEntity(Booking booking) {
        String vehicleInfo = booking.getVehicle() == null ? "" : String.format("%d %s %s",
                booking.getVehicle().getYear(), booking.getVehicle().getMake(), booking.getVehicle().getModel());
        return MechanicBookingSummary.builder()
                .id(booking.getId())
                .bookingNumber(booking.getBookingNumber())
                .customerName(booking.getCustomer() != null ? booking.getCustomer().getName() : "")
                .vehicleInfo(vehicleInfo)
                .serviceName(booking.getServiceOffering() != null ? booking.getServiceOffering().getName() : "")
                .status(booking.getStatus())
                .scheduledAt(booking.getScheduledAt())
                .build();
    }
}
