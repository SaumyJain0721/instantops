package com.instantops.service.service;

import com.instantops.service.dto.ServiceOfferingResponse;

import java.util.List;

public interface ServiceOfferingService {

    List<ServiceOfferingResponse> getAllServices();

    ServiceOfferingResponse getServiceById(Long id);
}
