package com.instantops.vehicle.entity;

import com.instantops.booking.entity.Booking;
import com.instantops.customer.entity.Customer;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vehicles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Customer is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @NotBlank(message = "Vehicle make is required")
    @Column(nullable = false, length = 50)
    private String make;

    @NotBlank(message = "Vehicle model is required")
    @Column(nullable = false, length = 50)
    private String model;

    @NotNull(message = "Vehicle year is required")
    @Column(nullable = false)
    private Integer year;

    @NotBlank(message = "License plate is required")
    @Column(name = "license_plate", nullable = false, unique = true, length = 20)
    private String licensePlate;

    @NotBlank(message = "VIN is required")
    @Column(nullable = false, unique = true, length = 30)
    private String vin;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "vehicle")
    @Builder.Default
    private List<Booking> bookings = new ArrayList<>();
}
