package com.instantops.booking.controller;

import com.instantops.booking.dto.BookingDetailResponse;
import com.instantops.booking.dto.BookingResponse;
import com.instantops.booking.dto.UpdateBookingStatusRequest;
import com.instantops.booking.entity.BookingStatus;
import com.instantops.booking.service.BookingService;
import com.instantops.common.ApiResponse;
import com.instantops.common.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@Tag(name = "Bookings", description = "Vehicle Service Booking Management & Status Operations")
public class BookingController {

    private final BookingService bookingService;

    @GetMapping
    @Operation(summary = "Search, filter, and paginate bookings",
               description = "Returns paginated bookings with full server-side search, status filter, mechanic filter, service filter, and sorting.")
    public ResponseEntity<ApiResponse<PageResponse<BookingResponse>>> getBookings(
            @Parameter(description = "Page index (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Search across booking number, customer, vehicle, license plate") @RequestParam(required = false) String search,
            @Parameter(description = "Filter by booking status") @RequestParam(required = false) BookingStatus status,
            @Parameter(description = "Filter by assigned mechanic ID") @RequestParam(required = false) Long mechanicId,
            @Parameter(description = "Filter by service offering ID") @RequestParam(required = false) Long serviceId,
            @Parameter(description = "Sort property (e.g. scheduledAt, createdAt, totalAmount, status)") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction (asc or desc)") @RequestParam(defaultValue = "desc") String sortDir
    ) {
        PageResponse<BookingResponse> result = bookingService.getBookings(
                page, size, search, status, mechanicId, serviceId, sortBy, sortDir
        );
        return ResponseEntity.ok(ApiResponse.ok(result, "Bookings retrieved successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get full booking details by ID",
               description = "Retrieves complete booking details including customer, vehicle, service package, and assigned mechanic.")
    public ResponseEntity<ApiResponse<BookingDetailResponse>> getBookingById(
            @Parameter(description = "Booking ID") @PathVariable Long id
    ) {
        BookingDetailResponse booking = bookingService.getBookingById(id);
        return ResponseEntity.ok(ApiResponse.ok(booking, "Booking details retrieved successfully"));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update booking status and resource assignment",
               description = "Transitions a booking to a new status (e.g. IN_PROGRESS, COMPLETED) with optional mechanic assignment.")
    public ResponseEntity<ApiResponse<BookingDetailResponse>> updateBookingStatus(
            @Parameter(description = "Booking ID") @PathVariable Long id,
            @Valid @RequestBody UpdateBookingStatusRequest request
    ) {
        BookingDetailResponse updated = bookingService.updateBookingStatus(id, request);
        return ResponseEntity.ok(ApiResponse.ok(updated, "Booking status updated successfully"));
    }
}
