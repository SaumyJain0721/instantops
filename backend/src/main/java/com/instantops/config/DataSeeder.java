package com.instantops.config;

import com.instantops.booking.entity.Booking;
import com.instantops.booking.entity.BookingStatus;
import com.instantops.booking.repository.BookingRepository;
import com.instantops.customer.entity.Customer;
import com.instantops.customer.repository.CustomerRepository;
import com.instantops.mechanic.entity.Mechanic;
import com.instantops.mechanic.entity.MechanicStatus;
import com.instantops.mechanic.repository.MechanicRepository;
import com.instantops.service.entity.ServiceOffering;
import com.instantops.service.repository.ServiceOfferingRepository;
import com.instantops.vehicle.entity.Vehicle;
import com.instantops.vehicle.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final CustomerRepository customerRepository;
    private final VehicleRepository vehicleRepository;
    private final MechanicRepository mechanicRepository;
    private final ServiceOfferingRepository serviceOfferingRepository;
    private final BookingRepository bookingRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (customerRepository.count() > 0) {
            log.info("Database already contains data ({} customers found). Skipping seed process.", customerRepository.count());
            return;
        }

        log.info("Starting InstantOps realistic seed data generation...");

        // 1. Seed Service Offerings (10 services)
        List<ServiceOffering> services = seedServices();
        log.info("Seeded {} Service Offerings", services.size());

        // 2. Seed Mechanics (25 mechanics)
        List<Mechanic> mechanics = seedMechanics();
        log.info("Seeded {} Mechanics", mechanics.size());

        // 3. Seed Customers (100 customers)
        List<Customer> customers = seedCustomers();
        log.info("Seeded {} Customers", customers.size());

        // 4. Seed Vehicles (150 vehicles across 100 customers)
        List<Vehicle> vehicles = seedVehicles(customers);
        log.info("Seeded {} Vehicles", vehicles.size());

        // 5. Seed Bookings (750 bookings)
        List<Booking> bookings = seedBookings(customers, vehicles, services, mechanics);
        log.info("Seeded {} Bookings across all 6 operational statuses", bookings.size());

        log.info("InstantOps Database Seed Completed Successfully!");
    }

    private List<ServiceOffering> seedServices() {
        List<ServiceOffering> list = List.of(
                ServiceOffering.builder()
                        .name("Periodic Maintenance")
                        .description("Comprehensive 30-point inspection, fluid top-up, spark plug check, and engine tune-up.")
                        .price(BigDecimal.valueOf(2499.00))
                        .estimatedDurationMinutes(120)
                        .build(),
                ServiceOffering.builder()
                        .name("Brake Pad Replacement")
                        .description("Front and rear brake disc and pad inspection, caliper servicing, and replacement.")
                        .price(BigDecimal.valueOf(1899.00))
                        .estimatedDurationMinutes(90)
                        .build(),
                ServiceOffering.builder()
                        .name("Engine Diagnostics")
                        .description("OBD-II computer scan, sensor calibration, live telemetry analysis, and diagnostic report.")
                        .price(BigDecimal.valueOf(1299.00))
                        .estimatedDurationMinutes(60)
                        .build(),
                ServiceOffering.builder()
                        .name("Oil & Filter Change")
                        .description("Fully synthetic engine oil replacement with premium OEM oil filter and drain plug gasket.")
                        .price(BigDecimal.valueOf(1499.00))
                        .estimatedDurationMinutes(45)
                        .build(),
                ServiceOffering.builder()
                        .name("AC Complete Servicing")
                        .description("AC condenser high-pressure wash, refrigerant R134a recharge, and cabin filter replacement.")
                        .price(BigDecimal.valueOf(2199.00))
                        .estimatedDurationMinutes(90)
                        .build(),
                ServiceOffering.builder()
                        .name("Battery Health Check & Replacement")
                        .description("Terminal anti-corrosion treatment, voltage load test, alternator check, and battery fitment.")
                        .price(BigDecimal.valueOf(899.00))
                        .estimatedDurationMinutes(30)
                        .build(),
                ServiceOffering.builder()
                        .name("Wheel Alignment & Balancing")
                        .description("3D laser computerized wheel alignment, digital dynamic balancing, and tire rotation.")
                        .price(BigDecimal.valueOf(999.00))
                        .estimatedDurationMinutes(45)
                        .build(),
                ServiceOffering.builder()
                        .name("Transmission Fluid Service")
                        .description("Automatic and manual transmission flush, pan gasket check, and synthetic ATF refill.")
                        .price(BigDecimal.valueOf(3499.00))
                        .estimatedDurationMinutes(120)
                        .build(),
                ServiceOffering.builder()
                        .name("Suspension & Steering Overhaul")
                        .description("Strut inspection, tie-rod end and ball joint check, bushing lubrication, and spring test.")
                        .price(BigDecimal.valueOf(4999.00))
                        .estimatedDurationMinutes(180)
                        .build(),
                ServiceOffering.builder()
                        .name("Full Body Ceramic Detailing")
                        .description("Clay bar paint decontamination, 3-stage rotary compounding, and 9H ceramic coating application.")
                        .price(BigDecimal.valueOf(7499.00))
                        .estimatedDurationMinutes(240)
                        .build()
        );
        return serviceOfferingRepository.saveAll(list);
    }

    private List<Mechanic> seedMechanics() {
        String[] firstNames = {
                "Rajesh", "Vikas", "Amit", "Suresh", "Manoj",
                "Arun", "Dinesh", "Pradeep", "Sanjay", "Ramesh",
                "Deepak", "Sunil", "Anil", "Ganesh", "Pankaj",
                "Santosh", "Mahesh", "Ashok", "Vijay", "Kishore",
                "Sachin", "Mohan", "Ravi", "Naveen", "Harish"
        };
        String[] lastNames = {
                "Kumar", "Patel", "Sharma", "Nair", "Verma",
                "Joshi", "Reddy", "Yadav", "Rao", "Kulkarni",
                "Gupta", "Deshmukh", "Mishra", "Hegde", "Chauhan",
                "Bhat", "Iyer", "Pandey", "Shinde", "Pillai",
                "Patil", "Das", "Shankar", "Swamy", "Sen"
        };
        String[] specs = {
                "Engine & Diagnostics", "Brakes & Suspension", "Electrical & AC",
                "Transmission Specialist", "General Maintenance", "EV Specialist"
        };
        MechanicStatus[] statuses = {
                MechanicStatus.AVAILABLE, MechanicStatus.ON_DUTY, MechanicStatus.BUSY,
                MechanicStatus.AVAILABLE, MechanicStatus.ON_DUTY, MechanicStatus.OFF_DUTY
        };

        List<Mechanic> list = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            String name = firstNames[i] + " " + lastNames[i];
            String email = firstNames[i].toLowerCase() + "." + lastNames[i].toLowerCase() + "@instantops.in";
            String phone = "+91 98" + String.format("%08d", 10000000 + i * 37461);
            String spec = specs[i % specs.length];
            MechanicStatus status = statuses[i % statuses.length];

            list.add(Mechanic.builder()
                    .name(name)
                    .email(email)
                    .phone(phone)
                    .specialization(spec)
                    .status(status)
                    .avatarUrl("https://images.unsplash.com/photo-" + (1535713875002L + i) + "?w=150&auto=format&fit=crop&q=80")
                    .build());
        }
        return mechanicRepository.saveAll(list);
    }

    private List<Customer> seedCustomers() {
        String[] firstNames = {
                "Aarav", "Priya", "Rohan", "Ananya", "Vikram", "Neha", "Aditya", "Pooja", "Karan", "Sneha",
                "Rahul", "Ritu", "Varun", "Tanvi", "Gaurav", "Divya", "Kunal", "Meera", "Siddharth", "Ishita",
                "Abhishek", "Kavita", "Nikhil", "Shreya", "Mayank", "Swati", "Harsh", "Deepika", "Manish", "Anjali",
                "Alok", "Simran", "Chetan", "Bhavna", "Vishal", "Rupal", "Tarun", "Payal", "Pranav", "Nidhi",
                "Kartik", "Shweta", "Sumit", "Garima", "Tushar", "Pallavi", "Akash", "Monika", "Yash", "Radhika"
        };
        String[] lastNames = {
                "Sharma", "Patel", "Mehta", "Iyer", "Singh", "Gupta", "Verma", "Nair", "Joshi", "Rao",
                "Chopra", "Reddy", "Kulkarni", "Deshmukh", "Bhat", "Kapoor", "Mishra", "Saxena", "Sen", "Bose",
                "Ghosh", "Malhotra", "Pandey", "Menon", "Pillai", "Chauhan", "Agarwal", "Dutta", "Nambiar", "Shetty"
        };
        String[] cities = {
                "Bandra West, Mumbai", "Koramangala, Bengaluru", "Indiranagar, Bengaluru", "Connaught Place, New Delhi",
                "Viman Nagar, Pune", "Kothrud, Pune", "Hitec City, Hyderabad", "Gachibowli, Hyderabad",
                "Anna Nagar, Chennai", "Salt Lake Sector V, Kolkata", "Satellite, Ahmedabad", "Vaishali Nagar, Jaipur"
        };

        Random rand = new Random(42);
        List<Customer> list = new ArrayList<>();
        Set<String> usedEmails = new HashSet<>();

        for (int i = 1; i <= 100; i++) {
            String first = firstNames[(i - 1) % firstNames.length];
            String last = lastNames[(i - 1 + rand.nextInt(lastNames.length)) % lastNames.length];
            String name = first + " " + last;

            String baseEmail = first.toLowerCase() + "." + last.toLowerCase();
            String email = baseEmail + (i > 50 ? i : "") + "@gmail.com";
            int suffix = 1;
            while (usedEmails.contains(email)) {
                email = baseEmail + suffix + "@gmail.com";
                suffix++;
            }
            usedEmails.add(email);

            String phone = "+91 97" + String.format("%08d", 10000000 + rand.nextInt(89999999));
            String address = (100 + rand.nextInt(900)) + ", " + cities[rand.nextInt(cities.length)];

            list.add(Customer.builder()
                    .name(name)
                    .email(email)
                    .phone(phone)
                    .address(address)
                    .build());
        }
        return customerRepository.saveAll(list);
    }

    private List<Vehicle> seedVehicles(List<Customer> customers) {
        String[][] carModels = {
                {"Hyundai", "Creta"}, {"Maruti Suzuki", "Swift"}, {"Tata", "Nexon"},
                {"Mahindra", "XUV700"}, {"Kia", "Seltos"}, {"Honda", "City"},
                {"Toyota", "Innova Crysta"}, {"Tata", "Punch"}, {"Hyundai", "i20"},
                {"Skoda", "Kushaq"}, {"Volkswagen", "Virtus"}, {"Maruti Suzuki", "Baleno"},
                {"MG", "Hector"}, {"Mahindra", "Thar"}, {"Tata", "Harrier"},
                {"Toyota", "Fortuner"}, {"Kia", "Sonet"}, {"Hyundai", "Verna"}
        };
        String[] stateCodes = {"MH12", "MH02", "KA05", "KA03", "DL01", "DL08", "TS09", "TS07", "HR26", "GJ01", "TN07", "WB02"};

        Random rand = new Random(101);
        List<Vehicle> list = new ArrayList<>();
        Set<String> usedPlates = new HashSet<>();
        Set<String> usedVins = new HashSet<>();

        // Generate 150 vehicles distributed among 100 customers (50 customers get 2 vehicles)
        for (int i = 0; i < 150; i++) {
            Customer customer = customers.get(i < 100 ? i : rand.nextInt(100));
            String[] car = carModels[rand.nextInt(carModels.length)];
            int year = 2017 + rand.nextInt(8); // 2017 - 2024

            String plate;
            do {
                String state = stateCodes[rand.nextInt(stateCodes.length)];
                char c1 = (char) ('A' + rand.nextInt(26));
                char c2 = (char) ('A' + rand.nextInt(26));
                int num = 1000 + rand.nextInt(9000);
                plate = state + c1 + c2 + num;
            } while (usedPlates.contains(plate));
            usedPlates.add(plate);

            String vin;
            do {
                vin = "MAT" + (10000000000000L + rand.nextLong(89999999999999L));
            } while (usedVins.contains(vin));
            usedVins.add(vin);

            list.add(Vehicle.builder()
                    .customer(customer)
                    .make(car[0])
                    .model(car[1])
                    .year(year)
                    .licensePlate(plate)
                    .vin(vin)
                    .build());
        }
        return vehicleRepository.saveAll(list);
    }

    private List<Booking> seedBookings(
            List<Customer> customers,
            List<Vehicle> vehicles,
            List<ServiceOffering> services,
            List<Mechanic> mechanics) {

        String[] generalNotes = {
                "Customer requested complimentary windshield washer fluid top-up.",
                "Vehicle making a mild metallic clicking noise during cold starts.",
                "Customer highlighted brake vibration on highway speeds.",
                "Routine 20,000 km periodic service interval check.",
                "Requested detailed report on tire tread depth & brake pad thickness.",
                "AC blower emitting mild odor; requested cabin filter replacement.",
                "Customer reported sudden drop in fuel efficiency.",
                "Check engine light illuminated intermittently.",
                "Battery warning indicator observed during morning ignition.",
                "Customer scheduled pre-monsoon comprehensive vehicle inspection."
        };

        String[] cancellationReasons = {
                "Customer cancelled due to emergency out-of-town travel.",
                "Rescheduled to next month per customer request.",
                "Customer decided to claim insurance for repair works.",
                "Vehicle sold by customer prior to service appointment."
        };

        Random rand = new Random(777);
        LocalDateTime now = LocalDateTime.now();
        List<Booking> list = new ArrayList<>();

        // Status targets: ~480 COMPLETED, ~95 IN_PROGRESS, ~60 ON_THE_WAY, ~50 ASSIGNED, ~40 PENDING, ~25 CANCELLED = 750
        BookingStatus[] statusPool = new BookingStatus[750];
        int idx = 0;
        for (int i = 0; i < 480; i++) statusPool[idx++] = BookingStatus.COMPLETED;
        for (int i = 0; i < 95; i++) statusPool[idx++] = BookingStatus.IN_PROGRESS;
        for (int i = 0; i < 60; i++) statusPool[idx++] = BookingStatus.ON_THE_WAY;
        for (int i = 0; i < 50; i++) statusPool[idx++] = BookingStatus.ASSIGNED;
        for (int i = 0; i < 40; i++) statusPool[idx++] = BookingStatus.PENDING;
        for (int i = 0; i < 25; i++) statusPool[idx++] = BookingStatus.CANCELLED;

        // Shuffle statuses so they distribute naturally across the order sequence
        List<BookingStatus> shuffledStatuses = Arrays.asList(statusPool);
        Collections.shuffle(shuffledStatuses, rand);

        for (int i = 0; i < 750; i++) {
            BookingStatus status = shuffledStatuses.get(i);
            String bookingNumber = "BKG-" + String.format("%05d", 10001 + i);

            Vehicle vehicle = vehicles.get(rand.nextInt(vehicles.size()));
            Customer customer = vehicle.getCustomer();
            ServiceOffering service = services.get(rand.nextInt(services.size()));

            Mechanic mechanic = null;
            if (status != BookingStatus.PENDING) {
                mechanic = mechanics.get(rand.nextInt(mechanics.size()));
            }

            // Distribute scheduledAt across past 45 days up to next 3 days
            LocalDateTime scheduledAt;
            LocalDateTime completedAt = null;

            if (status == BookingStatus.COMPLETED) {
                // Completed happened in the past (1 to 45 days ago)
                int daysAgo = 1 + rand.nextInt(44);
                scheduledAt = now.minusDays(daysAgo).withHour(9 + rand.nextInt(8)).withMinute(rand.nextInt(4) * 15).withSecond(0);
                completedAt = scheduledAt.plusMinutes(service.getEstimatedDurationMinutes() + rand.nextInt(30));
            } else if (status == BookingStatus.IN_PROGRESS || status == BookingStatus.ON_THE_WAY) {
                // Currently ongoing today or yesterday
                scheduledAt = now.minusHours(1 + rand.nextInt(6)).withMinute(0).withSecond(0);
            } else if (status == BookingStatus.ASSIGNED) {
                // Scheduled today or tomorrow
                scheduledAt = now.plusHours(1 + rand.nextInt(24)).withMinute(0).withSecond(0);
            } else if (status == BookingStatus.PENDING) {
                // Freshly booked today or upcoming
                scheduledAt = now.plusDays(rand.nextInt(3)).withHour(10 + rand.nextInt(6)).withMinute(0).withSecond(0);
            } else { // CANCELLED
                int daysAgo = rand.nextInt(30);
                scheduledAt = now.minusDays(daysAgo).withHour(10 + rand.nextInt(6)).withMinute(0).withSecond(0);
            }

            String notes = status == BookingStatus.CANCELLED
                    ? cancellationReasons[rand.nextInt(cancellationReasons.length)]
                    : generalNotes[rand.nextInt(generalNotes.length)];

            list.add(Booking.builder()
                    .bookingNumber(bookingNumber)
                    .customer(customer)
                    .vehicle(vehicle)
                    .serviceOffering(service)
                    .mechanic(mechanic)
                    .status(status)
                    .totalAmount(service.getPrice())
                    .scheduledAt(scheduledAt)
                    .completedAt(completedAt)
                    .notes(notes)
                    .build());
        }

        return bookingRepository.saveAll(list);
    }
}
