package com.instantops.booking.repository;

import com.instantops.booking.entity.Booking;
import com.instantops.booking.entity.BookingStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class BookingSpecification {

    public static Specification<Booking> filterBookings(
            String search,
            BookingStatus status,
            Long mechanicId,
            Long serviceId) {

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 1. Search across multiple fields (case-insensitive)
            if (StringUtils.hasText(search)) {
                String pattern = "%" + search.trim().toLowerCase() + "%";
                Predicate searchPredicate = criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("bookingNumber")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("customer").get("name")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("customer").get("phone")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("customer").get("email")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("vehicle").get("licensePlate")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("vehicle").get("make")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("vehicle").get("model")), pattern)
                );
                predicates.add(searchPredicate);
            }

            // 2. Status filter
            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            // 3. Mechanic filter
            if (mechanicId != null) {
                predicates.add(criteriaBuilder.equal(root.get("mechanic").get("id"), mechanicId));
            }

            // 4. Service filter
            if (serviceId != null) {
                predicates.add(criteriaBuilder.equal(root.get("serviceOffering").get("id"), serviceId));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
