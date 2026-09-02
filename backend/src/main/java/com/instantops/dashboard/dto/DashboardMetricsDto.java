package com.instantops.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardMetricsDto {

    private long totalBookings;
    private long todayBookings;
    private long completedBookings;
    private long pendingBookings;
    private long cancelledBookings;
    private BigDecimal totalRevenue;
    private long activeMechanics;
    private long newCustomers;
}
