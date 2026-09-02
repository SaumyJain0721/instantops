package com.instantops.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceBreakdownDto {

    private String serviceName;
    private long bookingCount;
    private BigDecimal revenue;
}
