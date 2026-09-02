package com.instantops;

import com.instantops.booking.entity.BookingStatus;
import com.instantops.booking.repository.BookingRepository;
import com.instantops.customer.repository.CustomerRepository;
import com.instantops.mechanic.entity.MechanicStatus;
import com.instantops.mechanic.repository.MechanicRepository;
import com.instantops.service.repository.ServiceOfferingRepository;
import com.instantops.vehicle.repository.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class InstantOpsApplicationTests {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private MechanicRepository mechanicRepository;

    @Autowired
    private ServiceOfferingRepository serviceOfferingRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Test
    void contextLoadsAndSeedDataIsPopulated() {
        // Verify Services
        assertThat(serviceOfferingRepository.count()).isGreaterThanOrEqualTo(10);

        // Verify Mechanics & Status diversity
        assertThat(mechanicRepository.count()).isGreaterThanOrEqualTo(25);
        assertThat(mechanicRepository.countByStatus(MechanicStatus.AVAILABLE)).isGreaterThan(0);
        assertThat(mechanicRepository.countByStatus(MechanicStatus.ON_DUTY)).isGreaterThan(0);
        assertThat(mechanicRepository.countByStatus(MechanicStatus.BUSY)).isGreaterThan(0);
        assertThat(mechanicRepository.countByStatus(MechanicStatus.OFF_DUTY)).isGreaterThan(0);

        // Verify Customers & Vehicles
        assertThat(customerRepository.count()).isGreaterThanOrEqualTo(100);
        assertThat(vehicleRepository.count()).isGreaterThanOrEqualTo(150);

        // Verify Bookings & All 6 Statuses
        assertThat(bookingRepository.count()).isGreaterThanOrEqualTo(750);
        assertThat(bookingRepository.countByStatus(BookingStatus.PENDING)).isGreaterThan(0);
        assertThat(bookingRepository.countByStatus(BookingStatus.ASSIGNED)).isGreaterThan(0);
        assertThat(bookingRepository.countByStatus(BookingStatus.ON_THE_WAY)).isGreaterThan(0);
        assertThat(bookingRepository.countByStatus(BookingStatus.IN_PROGRESS)).isGreaterThan(0);
        assertThat(bookingRepository.countByStatus(BookingStatus.COMPLETED)).isGreaterThan(0);
        assertThat(bookingRepository.countByStatus(BookingStatus.CANCELLED)).isGreaterThan(0);

        // Verify Revenue aggregation
        assertThat(bookingRepository.calculateTotalRevenue()).isNotNull();
        assertThat(bookingRepository.calculateTotalRevenue().doubleValue()).isGreaterThan(0);
    }
}
