package com.instantops.service.service.impl;

import com.instantops.common.exception.ResourceNotFoundException;
import com.instantops.service.dto.ServiceOfferingResponse;
import com.instantops.service.entity.ServiceOffering;
import com.instantops.service.repository.ServiceOfferingRepository;
import com.instantops.service.service.ServiceOfferingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ServiceOfferingServiceImpl implements ServiceOfferingService {

    private final ServiceOfferingRepository serviceOfferingRepository;

    @Override
    public List<ServiceOfferingResponse> getAllServices() {
        List<ServiceOffering> services = serviceOfferingRepository.findAll();
        return services.stream()
                .map(ServiceOfferingResponse::fromEntity)
                .toList();
    }

    @Override
    public ServiceOfferingResponse getServiceById(Long id) {
        ServiceOffering service = serviceOfferingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ServiceOffering", "id", id));
        return ServiceOfferingResponse.fromEntity(service);
    }
}
