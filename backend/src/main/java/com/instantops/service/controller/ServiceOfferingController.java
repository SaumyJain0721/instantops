package com.instantops.service.controller;

import com.instantops.common.ApiResponse;
import com.instantops.service.dto.ServiceOfferingResponse;
import com.instantops.service.service.ServiceOfferingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
@Tag(name = "Services", description = "Vehicle Service Packages & Pricing Catalog")
public class ServiceOfferingController {

    private final ServiceOfferingService serviceOfferingService;

    @GetMapping
    @Operation(summary = "List all vehicle service offerings",
               description = "Returns the active catalog of service packages including description, pricing (INR), and estimated durations.")
    public ResponseEntity<ApiResponse<List<ServiceOfferingResponse>>> getAllServices() {
        List<ServiceOfferingResponse> services = serviceOfferingService.getAllServices();
        return ResponseEntity.ok(ApiResponse.ok(services, "Service offerings retrieved successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get service package details by ID",
               description = "Retrieves details of a specific service offering.")
    public ResponseEntity<ApiResponse<ServiceOfferingResponse>> getServiceById(@PathVariable Long id) {
        ServiceOfferingResponse service = serviceOfferingService.getServiceById(id);
        return ResponseEntity.ok(ApiResponse.ok(service, "Service offering retrieved successfully"));
    }
}
