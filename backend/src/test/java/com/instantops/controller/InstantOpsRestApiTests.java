package com.instantops.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.instantops.booking.dto.UpdateBookingStatusRequest;
import com.instantops.booking.entity.Booking;
import com.instantops.booking.entity.BookingStatus;
import com.instantops.booking.repository.BookingRepository;
import com.instantops.customer.entity.Customer;
import com.instantops.customer.repository.CustomerRepository;
import com.instantops.mechanic.entity.Mechanic;
import com.instantops.mechanic.repository.MechanicRepository;
import com.instantops.service.repository.ServiceOfferingRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class InstantOpsRestApiTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private MechanicRepository mechanicRepository;

    @Autowired
    private ServiceOfferingRepository serviceOfferingRepository;

    @Test
    @DisplayName("GET /api/dashboard - Returns complete aggregated metrics, pulse, and chart arrays")
    void testGetDashboard() throws Exception {
        mockMvc.perform(get("/api/dashboard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.summary.totalBookings", greaterThan(0)))
                .andExpect(jsonPath("$.data.summary.totalRevenue", greaterThan(0.0)))
                .andExpect(jsonPath("$.data.summary.activeMechanics", greaterThan(0)))
                .andExpect(jsonPath("$.data.operationsPulse.completed", greaterThan(0)))
                .andExpect(jsonPath("$.data.operationsPulse.pending", greaterThan(0)))
                .andExpect(jsonPath("$.data.bookingsOverTime", hasSize(14)))
                .andExpect(jsonPath("$.data.revenueOverTime", hasSize(14)))
                .andExpect(jsonPath("$.data.revenueOverTime[*].revenue", notNullValue()))
                .andExpect(jsonPath("$.data.statusDistribution", hasSize(6)))
                .andExpect(jsonPath("$.data.serviceBreakdown", hasSize(10)))
                .andExpect(jsonPath("$.data.recentActivity", hasSize(greaterThan(0))));
    }

    @Test
    @DisplayName("GET /api/bookings - Supports pagination, search, filtering, and sorting")
    void testGetBookingsWithFilters() throws Exception {
        // 1. Basic pagination
        mockMvc.perform(get("/api/bookings")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "createdAt")
                        .param("sortDir", "desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.content", hasSize(10)))
                .andExpect(jsonPath("$.data.totalElements", greaterThan(500)))
                .andExpect(jsonPath("$.data.totalPages", greaterThan(1)));

        // 2. Filter by status
        mockMvc.perform(get("/api/bookings")
                        .param("status", "COMPLETED")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].status", is("COMPLETED")));

        // 3. Search query
        mockMvc.perform(get("/api/bookings")
                        .param("search", "BKG-100")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content", hasSize(greaterThan(0))));
    }

    @Test
    @DisplayName("GET /api/bookings/{id} - Returns nested booking details")
    void testGetBookingById() throws Exception {
        Booking booking = bookingRepository.findAll().getFirst();

        mockMvc.perform(get("/api/bookings/" + booking.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.id", is(booking.getId().intValue())))
                .andExpect(jsonPath("$.data.bookingNumber", is(booking.getBookingNumber())))
                .andExpect(jsonPath("$.data.customer.name", notNullValue()))
                .andExpect(jsonPath("$.data.vehicle.make", notNullValue()))
                .andExpect(jsonPath("$.data.service.name", notNullValue()));
    }

    @Test
    @DisplayName("PATCH /api/bookings/{id}/status - Updates status and assigns mechanic")
    void testUpdateBookingStatus() throws Exception {
        // Find a pending booking
        Booking pendingBooking = bookingRepository.findByStatus(BookingStatus.PENDING).getFirst();
        Mechanic mechanic = mechanicRepository.findAll().getFirst();

        UpdateBookingStatusRequest request = UpdateBookingStatusRequest.builder()
                .status(BookingStatus.IN_PROGRESS)
                .mechanicId(mechanic.getId())
                .notes("Started urgent brake servicing")
                .build();

        mockMvc.perform(patch("/api/bookings/" + pendingBooking.getId() + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.status", is("IN_PROGRESS")))
                .andExpect(jsonPath("$.data.mechanic.name", is(mechanic.getName())));
    }

    @Test
    @DisplayName("GET /api/mechanics & /api/mechanics/{id}")
    void testGetMechanics() throws Exception {
        mockMvc.perform(get("/api/mechanics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", hasSize(25)))
                .andExpect(jsonPath("$.data[0].name", notNullValue()));

        Mechanic mechanic = mechanicRepository.findAll().getFirst();
        mockMvc.perform(get("/api/mechanics/" + mechanic.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id", is(mechanic.getId().intValue())))
                .andExpect(jsonPath("$.data.name", is(mechanic.getName())));
    }

    @Test
    @DisplayName("GET /api/customers & /api/customers/{id}")
    void testGetCustomers() throws Exception {
        mockMvc.perform(get("/api/customers").param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.content", hasSize(10)))
                .andExpect(jsonPath("$.data.totalElements", is(100)));

        Customer customer = customerRepository.findAll().getFirst();
        mockMvc.perform(get("/api/customers/" + customer.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id", is(customer.getId().intValue())))
                .andExpect(jsonPath("$.data.vehicles", hasSize(greaterThan(0))));
    }

    @Test
    @DisplayName("GET /api/services")
    void testGetServices() throws Exception {
        mockMvc.perform(get("/api/services"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", hasSize(10)))
                .andExpect(jsonPath("$.data[0].price", greaterThan(0.0)));
    }
}
