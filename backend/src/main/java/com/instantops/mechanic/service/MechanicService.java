package com.instantops.mechanic.service;

import com.instantops.mechanic.dto.MechanicResponse;
import com.instantops.mechanic.entity.MechanicStatus;

import java.util.List;

public interface MechanicService {

    List<MechanicResponse> getMechanics(MechanicStatus status);

    MechanicResponse getMechanicById(Long id);
}
