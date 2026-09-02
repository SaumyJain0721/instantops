package com.instantops.vehicle.dto;

import com.instantops.vehicle.entity.Vehicle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleResponse {

    private Long id;
    private Long customerId;
    private String customerName;
    private String make;
    private String model;
    private Integer year;
    private String licensePlate;
    private String vin;
    private LocalDateTime createdAt;

    public static VehicleResponse fromEntity(Vehicle vehicle) {
        return VehicleResponse.builder()
                .id(vehicle.getId())
                .customerId(vehicle.getCustomer() != null ? vehicle.getCustomer().getId() : null)
                .customerName(vehicle.getCustomer() != null ? vehicle.getCustomer().getName() : null)
                .make(vehicle.getMake())
                .model(vehicle.getModel())
                .year(vehicle.getYear())
                .licensePlate(vehicle.getLicensePlate())
                .vin(vehicle.getVin())
                .createdAt(vehicle.getCreatedAt())
                .build();
    }
}
