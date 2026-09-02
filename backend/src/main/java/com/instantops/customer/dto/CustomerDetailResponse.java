package com.instantops.customer.dto;

import com.instantops.customer.entity.Customer;
import com.instantops.vehicle.dto.VehicleResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDetailResponse {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;
    @Builder.Default
    private List<VehicleResponse> vehicles = new ArrayList<>();
    private int bookingCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CustomerDetailResponse fromEntity(Customer customer) {
        return CustomerDetailResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .address(customer.getAddress())
                .vehicles(customer.getVehicles() != null ?
                        customer.getVehicles().stream().map(VehicleResponse::fromEntity).toList() : new ArrayList<>())
                .bookingCount(customer.getBookings() != null ? customer.getBookings().size() : 0)
                .createdAt(customer.getCreatedAt())
                .updatedAt(customer.getUpdatedAt())
                .build();
    }
}
