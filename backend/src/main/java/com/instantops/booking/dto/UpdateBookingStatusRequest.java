package com.instantops.booking.dto;

import com.instantops.booking.entity.BookingStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBookingStatusRequest {

    @NotNull(message = "Target status is required")
    private BookingStatus status;

    private Long mechanicId;

    private String notes;
}
