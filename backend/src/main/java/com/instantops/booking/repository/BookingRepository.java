package com.instantops.booking.repository;

import com.instantops.booking.entity.Booking;
import com.instantops.booking.entity.BookingStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long>, JpaSpecificationExecutor<Booking> {

    Optional<Booking> findByBookingNumber(String bookingNumber);

    boolean existsByBookingNumber(String bookingNumber);

    List<Booking> findByCustomerId(Long customerId);

    List<Booking> findByMechanicId(Long mechanicId);

    List<Booking> findByStatus(BookingStatus status);

    long countByStatus(BookingStatus status);

    long countByScheduledAtBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT COALESCE(SUM(b.totalAmount), 0) FROM Booking b WHERE b.status = com.instantops.booking.entity.BookingStatus.COMPLETED")
    BigDecimal calculateTotalRevenue();

    @Query("SELECT b.status, COUNT(b) FROM Booking b GROUP BY b.status")
    List<Object[]> countBookingsByStatusGroup();

    @Query("SELECT b.serviceOffering.name, COUNT(b), COALESCE(SUM(b.totalAmount), 0) FROM Booking b " +
           "WHERE b.status != com.instantops.booking.entity.BookingStatus.CANCELLED " +
           "GROUP BY b.serviceOffering.name ORDER BY COUNT(b) DESC")
    List<Object[]> getServiceBreakdownGroup();

    @Query("SELECT CAST(b.scheduledAt AS date), COUNT(b) FROM Booking b " +
           "WHERE b.scheduledAt >= :startDate " +
           "GROUP BY CAST(b.scheduledAt AS date) " +
           "ORDER BY CAST(b.scheduledAt AS date) ASC")
    List<Object[]> getDailyBookingCountsSince(@Param("startDate") LocalDateTime startDate);

    @Query("SELECT CAST(b.scheduledAt AS date), COALESCE(SUM(b.totalAmount), 0) FROM Booking b " +
           "WHERE b.scheduledAt >= :startDate AND b.status = com.instantops.booking.entity.BookingStatus.COMPLETED " +
           "GROUP BY CAST(b.scheduledAt AS date) " +
           "ORDER BY CAST(b.scheduledAt AS date) ASC")
    List<Object[]> getDailyRevenueSince(@Param("startDate") LocalDateTime startDate);

    @Query("SELECT b FROM Booking b JOIN FETCH b.customer JOIN FETCH b.vehicle JOIN FETCH b.serviceOffering LEFT JOIN FETCH b.mechanic WHERE b.id = :id")
    Optional<Booking> findByIdWithDetails(@Param("id") Long id);

    @Query("SELECT b FROM Booking b JOIN FETCH b.customer JOIN FETCH b.vehicle JOIN FETCH b.serviceOffering LEFT JOIN FETCH b.mechanic ORDER BY b.createdAt DESC")
    List<Booking> findRecentBookings(Pageable pageable);
}
