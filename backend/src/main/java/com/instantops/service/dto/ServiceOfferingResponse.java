package com.instantops.service.dto;

import com.instantops.service.entity.ServiceOffering;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceOfferingResponse {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer estimatedDurationMinutes;
    private LocalDateTime createdAt;

    public static ServiceOfferingResponse fromEntity(ServiceOffering service) {
        return ServiceOfferingResponse.builder()
                .id(service.getId())
                .name(service.getName())
                .description(service.getDescription())
                .price(service.getPrice())
                .estimatedDurationMinutes(service.getEstimatedDurationMinutes())
                .createdAt(service.getCreatedAt())
                .build();
    }
}
