package com.instantops.common.exception;

import com.instantops.booking.entity.BookingStatus;

public class InvalidStatusTransitionException extends RuntimeException {

    public InvalidStatusTransitionException(BookingStatus currentStatus, BookingStatus targetStatus) {
        super(String.format("Invalid booking status transition from '%s' to '%s'", currentStatus, targetStatus));
    }

    public InvalidStatusTransitionException(String message) {
        super(message);
    }
}
