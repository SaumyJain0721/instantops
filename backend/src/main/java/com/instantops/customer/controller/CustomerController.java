package com.instantops.customer.controller;

import com.instantops.common.ApiResponse;
import com.instantops.common.PageResponse;
import com.instantops.customer.dto.CustomerDetailResponse;
import com.instantops.customer.dto.CustomerResponse;
import com.instantops.customer.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@Tag(name = "Customers", description = "Customer Accounts & Vehicle Ownership Records")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    @Operation(summary = "Search and paginate customers",
               description = "Returns paginated list of customers with optional name/phone/email search filter.")
    public ResponseEntity<ApiResponse<PageResponse<CustomerResponse>>> getCustomers(
            @Parameter(description = "Page index (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Search query for name, email, or phone") @RequestParam(required = false) String search
    ) {
        PageResponse<CustomerResponse> customers = customerService.getCustomers(page, size, search);
        return ResponseEntity.ok(ApiResponse.ok(customers, "Customers retrieved successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get customer details by ID",
               description = "Retrieves full customer profile including their registered vehicles and booking history count.")
    public ResponseEntity<ApiResponse<CustomerDetailResponse>> getCustomerById(
            @Parameter(description = "Customer ID") @PathVariable Long id
    ) {
        CustomerDetailResponse customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(ApiResponse.ok(customer, "Customer details retrieved successfully"));
    }
}
