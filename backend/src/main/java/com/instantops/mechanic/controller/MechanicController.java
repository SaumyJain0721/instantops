package com.instantops.mechanic.controller;

import com.instantops.common.ApiResponse;
import com.instantops.mechanic.dto.MechanicResponse;
import com.instantops.mechanic.entity.MechanicStatus;
import com.instantops.mechanic.service.MechanicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mechanics")
@RequiredArgsConstructor
@Tag(name = "Mechanics", description = "Mechanic Profiles & Workload Allocation")
public class MechanicController {

    private final MechanicService mechanicService;

    @GetMapping
    @Operation(summary = "List all mechanics",
               description = "Returns all mechanics with their current availability status and active assigned booking count.")
    public ResponseEntity<ApiResponse<List<MechanicResponse>>> getMechanics(
            @Parameter(description = "Filter by mechanic availability status") @RequestParam(required = false) MechanicStatus status
    ) {
        List<MechanicResponse> mechanics = mechanicService.getMechanics(status);
        return ResponseEntity.ok(ApiResponse.ok(mechanics, "Mechanics retrieved successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get mechanic details by ID",
               description = "Retrieves a mechanic's complete profile and current operational status.")
    public ResponseEntity<ApiResponse<MechanicResponse>> getMechanicById(
            @Parameter(description = "Mechanic ID") @PathVariable Long id
    ) {
        MechanicResponse mechanic = mechanicService.getMechanicById(id);
        return ResponseEntity.ok(ApiResponse.ok(mechanic, "Mechanic details retrieved successfully"));
    }
}
