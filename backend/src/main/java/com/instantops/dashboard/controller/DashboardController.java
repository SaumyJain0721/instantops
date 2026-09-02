package com.instantops.dashboard.controller;

import com.instantops.common.ApiResponse;
import com.instantops.dashboard.dto.DashboardResponse;
import com.instantops.dashboard.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "Operations Dashboard Analytics & Real-Time KPIs")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    @Operation(summary = "Get live operations dashboard metrics",
               description = "Returns aggregated KPIs, operations pulse breakdown, time-series chart data, status distribution, and recent activity.")
    public ResponseEntity<ApiResponse<DashboardResponse>> getDashboard() {
        DashboardResponse data = dashboardService.getDashboardData();
        return ResponseEntity.ok(ApiResponse.ok(data, "Dashboard metrics retrieved successfully"));
    }
}
