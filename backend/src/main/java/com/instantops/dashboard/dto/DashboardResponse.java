package com.instantops.dashboard.dto;

import com.instantops.booking.dto.BookingResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {

    private DashboardMetricsDto summary;
    private OperationsPulseDto operationsPulse;

    @Builder.Default
    private List<TimeSeriesDataPoint> bookingsOverTime = new ArrayList<>();

    @Builder.Default
    private List<TimeSeriesDataPoint> revenueOverTime = new ArrayList<>();

    @Builder.Default
    private List<StatusDistributionDto> statusDistribution = new ArrayList<>();

    @Builder.Default
    private List<ServiceBreakdownDto> serviceBreakdown = new ArrayList<>();

    @Builder.Default
    private List<BookingResponse> recentActivity = new ArrayList<>();
}
