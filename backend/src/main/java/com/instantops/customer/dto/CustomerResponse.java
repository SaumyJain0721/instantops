package com.instantops.customer.dto;

import com.instantops.customer.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponse {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private int vehicleCount;
    private int bookingCount;
    private LocalDateTime createdAt;

    public static CustomerResponse fromEntity(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .address(customer.getAddress())
                .vehicleCount(customer.getVehicles() != null ? customer.getVehicles().size() : 0)
                .bookingCount(customer.getBookings() != null ? customer.getBookings().size() : 0)
                .createdAt(customer.getCreatedAt())
                .build();
    }
}
