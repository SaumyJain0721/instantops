package com.instantops.mechanic.repository;

import com.instantops.mechanic.entity.Mechanic;
import com.instantops.mechanic.entity.MechanicStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MechanicRepository extends JpaRepository<Mechanic, Long> {

    Optional<Mechanic> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Mechanic> findByStatus(MechanicStatus status);

    long countByStatus(MechanicStatus status);
}
