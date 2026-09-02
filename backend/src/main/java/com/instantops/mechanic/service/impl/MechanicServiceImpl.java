package com.instantops.mechanic.service.impl;

import com.instantops.common.exception.ResourceNotFoundException;
import com.instantops.mechanic.dto.MechanicResponse;
import com.instantops.mechanic.entity.Mechanic;
import com.instantops.mechanic.entity.MechanicStatus;
import com.instantops.mechanic.repository.MechanicRepository;
import com.instantops.mechanic.service.MechanicService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MechanicServiceImpl implements MechanicService {

    private final MechanicRepository mechanicRepository;

    @Override
    public List<MechanicResponse> getMechanics(MechanicStatus status) {
        List<Mechanic> mechanics = (status != null)
                ? mechanicRepository.findByStatus(status)
                : mechanicRepository.findAll();

        return mechanics.stream()
                .map(MechanicResponse::fromEntity)
                .toList();
    }

    @Override
    public MechanicResponse getMechanicById(Long id) {
        Mechanic mechanic = mechanicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mechanic", "id", id));
        return MechanicResponse.fromEntity(mechanic);
    }
}
