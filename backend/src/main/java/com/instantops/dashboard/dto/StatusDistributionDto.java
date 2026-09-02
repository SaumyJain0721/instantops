package com.instantops.dashboard.dto;

import com.instantops.booking.entity.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatusDistributionDto {

    private BookingStatus status;
    private long count;
    private double percentage;
}
