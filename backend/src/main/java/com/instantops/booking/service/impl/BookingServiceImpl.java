package com.instantops.booking.service.impl;

import com.instantops.booking.dto.BookingDetailResponse;
import com.instantops.booking.dto.BookingResponse;
import com.instantops.booking.dto.UpdateBookingStatusRequest;
import com.instantops.booking.entity.Booking;
import com.instantops.booking.entity.BookingStatus;
import com.instantops.booking.repository.BookingRepository;
import com.instantops.booking.repository.BookingSpecification;
import com.instantops.booking.service.BookingService;
import com.instantops.common.PageResponse;
import com.instantops.common.exception.InvalidStatusTransitionException;
import com.instantops.common.exception.ResourceNotFoundException;
import com.instantops.mechanic.entity.Mechanic;
import com.instantops.mechanic.repository.MechanicRepository;
import com.instantops.sse.event.BookingStatusChangedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final MechanicRepository mechanicRepository;
    private final ApplicationEventPublisher eventPublisher;

    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
            "id", "bookingNumber", "scheduledAt", "createdAt", "totalAmount", "status"
    );

    @Override
    @Transactional(readOnly = true)
    public PageResponse<BookingResponse> getBookings(
            int page,
            int size,
            String search,
            BookingStatus status,
            Long mechanicId,
            Long serviceId,
            String sortBy,
            String sortDir) {

        // 1. Resolve sorting
        String sortProperty = (StringUtils.hasText(sortBy) && ALLOWED_SORT_FIELDS.contains(sortBy)) ? sortBy : "createdAt";
        Sort.Direction direction = "asc".equalsIgnoreCase(sortDir) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortProperty);

        Pageable pageable = PageRequest.of(Math.max(0, page), Math.max(1, size), sort);

        // 2. Build dynamic specification
        Specification<Booking> spec = BookingSpecification.filterBookings(search, status, mechanicId, serviceId);

        // 3. Query & Map
        Page<Booking> bookingPage = bookingRepository.findAll(spec, pageable);
        Page<BookingResponse> responsePage = bookingPage.map(BookingResponse::fromEntity);

        return PageResponse.of(responsePage);
    }

    @Override
    @Transactional(readOnly = true)
    public BookingDetailResponse getBookingById(Long id) {
        Booking booking = bookingRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", id));
        return BookingDetailResponse.fromEntity(booking);
    }

    @Override
    @Transactional
    public BookingDetailResponse updateBookingStatus(Long id, UpdateBookingStatusRequest request) {
        Booking booking = bookingRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", id));

        BookingStatus currentStatus = booking.getStatus();
        BookingStatus targetStatus = request.getStatus();

        // 1. Validate status transition
        validateStatusTransition(currentStatus, targetStatus);

        booking.setStatus(targetStatus);

        // 2. Assign or change mechanic if provided
        if (request.getMechanicId() != null) {
            Mechanic mechanic = mechanicRepository.findById(request.getMechanicId())
                    .orElseThrow(() -> new ResourceNotFoundException("Mechanic", "id", request.getMechanicId()));
            booking.setMechanic(mechanic);
        }

        // 3. Update completedAt timestamp on COMPLETED
        if (targetStatus == BookingStatus.COMPLETED && booking.getCompletedAt() == null) {
            booking.setCompletedAt(LocalDateTime.now());
        }

        // 4. Update notes if supplied
        if (StringUtils.hasText(request.getNotes())) {
            String existing = booking.getNotes();
            if (StringUtils.hasText(existing)) {
                booking.setNotes(existing + " | " + request.getNotes().trim());
            } else {
                booking.setNotes(request.getNotes().trim());
            }
        }

        Booking updated = bookingRepository.save(booking);
        log.info("Booking {} status updated from {} to {}", updated.getBookingNumber(), currentStatus, targetStatus);

        // 5. Publish real-time domain event for SSE broadcast
        String vehicleInfo = updated.getVehicle() != null
                ? String.format("%d %s %s", updated.getVehicle().getYear(), updated.getVehicle().getMake(), updated.getVehicle().getModel())
                : "";

        BookingStatusChangedEvent event = BookingStatusChangedEvent.builder()
                .bookingId(updated.getId())
                .bookingNumber(updated.getBookingNumber())
                .previousStatus(currentStatus)
                .newStatus(targetStatus)
                .customerName(updated.getCustomer() != null ? updated.getCustomer().getName() : "")
                .customerPhone(updated.getCustomer() != null ? updated.getCustomer().getPhone() : "")
                .vehicleInfo(vehicleInfo)
                .licensePlate(updated.getVehicle() != null ? updated.getVehicle().getLicensePlate() : "")
                .serviceName(updated.getServiceOffering() != null ? updated.getServiceOffering().getName() : "")
                .mechanicName(updated.getMechanic() != null ? updated.getMechanic().getName() : "Unassigned")
                .totalAmount(updated.getTotalAmount())
                .notes(updated.getNotes())
                .eventType("BOOKING_STATUS_CHANGED")
                .timestamp(LocalDateTime.now())
                .build();

        eventPublisher.publishEvent(event);

        return BookingDetailResponse.fromEntity(updated);
    }

    private void validateStatusTransition(BookingStatus from, BookingStatus to) {
        if (from == to) {
            return; // No-op status update (e.g. updating notes or mechanic assignment)
        }

        if (from == BookingStatus.COMPLETED) {
            throw new InvalidStatusTransitionException(
                    String.format("Cannot transition booking from terminal state '%s' to '%s'", from, to));
        }

        if (from == BookingStatus.CANCELLED) {
            throw new InvalidStatusTransitionException(
                    String.format("Cannot transition booking from terminal state '%s' to '%s'", from, to));
        }

        // Valid forward or operational transitions
        boolean valid = switch (from) {
            case PENDING -> to == BookingStatus.ASSIGNED || to == BookingStatus.ON_THE_WAY || to == BookingStatus.IN_PROGRESS || to == BookingStatus.CANCELLED;
            case ASSIGNED -> to == BookingStatus.ON_THE_WAY || to == BookingStatus.IN_PROGRESS || to == BookingStatus.PENDING || to == BookingStatus.CANCELLED;
            case ON_THE_WAY -> to == BookingStatus.IN_PROGRESS || to == BookingStatus.ASSIGNED || to == BookingStatus.CANCELLED;
            case IN_PROGRESS -> to == BookingStatus.COMPLETED || to == BookingStatus.CANCELLED || to == BookingStatus.ON_THE_WAY;
            default -> false;
        };

        if (!valid) {
            throw new InvalidStatusTransitionException(from, to);
        }
    }
}
