package com.instantops.customer.service.impl;

import com.instantops.common.PageResponse;
import com.instantops.common.exception.ResourceNotFoundException;
import com.instantops.customer.dto.CustomerDetailResponse;
import com.instantops.customer.dto.CustomerResponse;
import com.instantops.customer.entity.Customer;
import com.instantops.customer.repository.CustomerRepository;
import com.instantops.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public PageResponse<CustomerResponse> getCustomers(int page, int size, String search) {
        Pageable pageable = PageRequest.of(Math.max(0, page), Math.max(1, size), Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Customer> customerPage = StringUtils.hasText(search)
                ? customerRepository.searchCustomers(search.trim(), pageable)
                : customerRepository.findAll(pageable);

        Page<CustomerResponse> responsePage = customerPage.map(CustomerResponse::fromEntity);
        return PageResponse.of(responsePage);
    }

    @Override
    public CustomerDetailResponse getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));
        return CustomerDetailResponse.fromEntity(customer);
    }
}
