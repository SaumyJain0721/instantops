package com.instantops.booking.service;

import com.instantops.booking.dto.BookingDetailResponse;
import com.instantops.booking.dto.BookingResponse;
import com.instantops.booking.dto.UpdateBookingStatusRequest;
import com.instantops.booking.entity.BookingStatus;
import com.instantops.common.PageResponse;

public interface BookingService {

    PageResponse<BookingResponse> getBookings(
            int page,
            int size,
            String search,
            BookingStatus status,
            Long mechanicId,
            Long serviceId,
            String sortBy,
            String sortDir
    );

    BookingDetailResponse getBookingById(Long id);

    BookingDetailResponse updateBookingStatus(Long id, UpdateBookingStatusRequest request);
}
