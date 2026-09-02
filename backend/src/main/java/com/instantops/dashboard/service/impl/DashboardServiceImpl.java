package com.instantops.dashboard.service.impl;

import com.instantops.booking.dto.BookingResponse;
import com.instantops.booking.entity.Booking;
import com.instantops.booking.entity.BookingStatus;
import com.instantops.booking.repository.BookingRepository;
import com.instantops.customer.repository.CustomerRepository;
import com.instantops.dashboard.dto.*;
import com.instantops.dashboard.service.DashboardService;
import com.instantops.mechanic.entity.MechanicStatus;
import com.instantops.mechanic.repository.MechanicRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final BookingRepository bookingRepository;
    private final MechanicRepository mechanicRepository;
    private final CustomerRepository customerRepository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public DashboardResponse getDashboardData() {
        log.debug("Calculating live operations dashboard metrics from database");

        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        LocalDateTime todayEnd = LocalDate.now().atTime(LocalTime.MAX);
        LocalDateTime fourteenDaysAgo = LocalDate.now().minusDays(13).atStartOfDay();

        // 1. Summary Metrics
        long totalBookings = bookingRepository.count();
        long todayBookings = bookingRepository.countByScheduledAtBetween(todayStart, todayEnd);
        long completedBookings = bookingRepository.countByStatus(BookingStatus.COMPLETED);
        long pendingBookings = bookingRepository.countByStatus(BookingStatus.PENDING);
        long cancelledBookings = bookingRepository.countByStatus(BookingStatus.CANCELLED);
        BigDecimal totalRevenue = bookingRepository.calculateTotalRevenue();

        long activeMechanics = mechanicRepository.countByStatus(MechanicStatus.AVAILABLE)
                + mechanicRepository.countByStatus(MechanicStatus.ON_DUTY)
                + mechanicRepository.countByStatus(MechanicStatus.BUSY);

        long newCustomers = customerRepository.countByCreatedAtAfter(LocalDate.now().minusDays(30).atStartOfDay());
        if (newCustomers == 0) {
            newCustomers = customerRepository.count();
        }

        DashboardMetricsDto summary = DashboardMetricsDto.builder()
                .totalBookings(totalBookings)
                .todayBookings(todayBookings)
                .completedBookings(completedBookings)
                .pendingBookings(pendingBookings)
                .cancelledBookings(cancelledBookings)
                .totalRevenue(totalRevenue != null ? totalRevenue : BigDecimal.ZERO)
                .activeMechanics(activeMechanics)
                .newCustomers(newCustomers)
                .build();

        // 2. Operations Pulse (all 6 statuses)
        long assignedCount = bookingRepository.countByStatus(BookingStatus.ASSIGNED);
        long onTheWayCount = bookingRepository.countByStatus(BookingStatus.ON_THE_WAY);
        long inProgressCount = bookingRepository.countByStatus(BookingStatus.IN_PROGRESS);

        OperationsPulseDto operationsPulse = OperationsPulseDto.builder()
                .pending(pendingBookings)
                .assigned(assignedCount)
                .onTheWay(onTheWayCount)
                .inProgress(inProgressCount)
                .completed(completedBookings)
                .cancelled(cancelledBookings)
                .build();

        // 3. Bookings & Revenue Over Time (Past 14 days time series)
        Map<String, Long> bookingCountsByDate = new LinkedHashMap<>();
        Map<String, BigDecimal> revenueByDate = new LinkedHashMap<>();

        // Initialize last 14 days with zero
        for (int i = 13; i >= 0; i--) {
            String dateStr = LocalDate.now().minusDays(i).format(DATE_FORMATTER);
            bookingCountsByDate.put(dateStr, 0L);
            revenueByDate.put(dateStr, BigDecimal.ZERO);
        }

        List<Object[]> dailyBookingResults = bookingRepository.getDailyBookingCountsSince(fourteenDaysAgo);
        for (Object[] row : dailyBookingResults) {
            String dateStr = row[0].toString();
            long count = ((Number) row[1]).longValue();
            if (bookingCountsByDate.containsKey(dateStr)) {
                bookingCountsByDate.put(dateStr, count);
            }
        }

        List<Object[]> dailyRevenueResults = bookingRepository.getDailyRevenueSince(fourteenDaysAgo);
        for (Object[] row : dailyRevenueResults) {
            String dateStr = row[0].toString();
            BigDecimal rev = row[1] instanceof BigDecimal ? (BigDecimal) row[1] : BigDecimal.valueOf(((Number) row[1]).doubleValue());
            if (revenueByDate.containsKey(dateStr)) {
                revenueByDate.put(dateStr, rev);
            }
        }

        List<TimeSeriesDataPoint> bookingsOverTime = new ArrayList<>();
        List<TimeSeriesDataPoint> revenueOverTime = new ArrayList<>();

        for (Map.Entry<String, Long> entry : bookingCountsByDate.entrySet()) {
            bookingsOverTime.add(TimeSeriesDataPoint.builder()
                    .date(entry.getKey())
                    .count(entry.getValue())
                    .build());
        }

        for (Map.Entry<String, BigDecimal> entry : revenueByDate.entrySet()) {
            revenueOverTime.add(TimeSeriesDataPoint.builder()
                    .date(entry.getKey())
                    .revenue(entry.getValue())
                    .build());
        }

        // 4. Status Distribution
        List<StatusDistributionDto> statusDistribution = new ArrayList<>();
        if (totalBookings > 0) {
            for (BookingStatus status : BookingStatus.values()) {
                long count = bookingRepository.countByStatus(status);
                double percentage = BigDecimal.valueOf((double) count * 100 / totalBookings)
                        .setScale(1, RoundingMode.HALF_UP)
                        .doubleValue();
                statusDistribution.add(StatusDistributionDto.builder()
                        .status(status)
                        .count(count)
                        .percentage(percentage)
                        .build());
            }
        }

        // 5. Service Breakdown
        List<ServiceBreakdownDto> serviceBreakdown = new ArrayList<>();
        List<Object[]> serviceResults = bookingRepository.getServiceBreakdownGroup();
        for (Object[] row : serviceResults) {
            String serviceName = (String) row[0];
            long count = ((Number) row[1]).longValue();
            BigDecimal rev = row[2] instanceof BigDecimal ? (BigDecimal) row[2] : BigDecimal.valueOf(((Number) row[2]).doubleValue());
            serviceBreakdown.add(ServiceBreakdownDto.builder()
                    .serviceName(serviceName)
                    .bookingCount(count)
                    .revenue(rev)
                    .build());
        }

        // 6. Recent Activity Feed (8 latest records)
        List<Booking> recentBookings = bookingRepository.findRecentBookings(PageRequest.of(0, 8));
        List<BookingResponse> recentActivity = recentBookings.stream()
                .map(BookingResponse::fromEntity)
                .toList();

        return DashboardResponse.builder()
                .summary(summary)
                .operationsPulse(operationsPulse)
                .bookingsOverTime(bookingsOverTime)
                .revenueOverTime(revenueOverTime)
                .statusDistribution(statusDistribution)
                .serviceBreakdown(serviceBreakdown)
                .recentActivity(recentActivity)
                .build();
    }
}
