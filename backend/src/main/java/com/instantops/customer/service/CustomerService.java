package com.instantops.customer.service;

import com.instantops.common.PageResponse;
import com.instantops.customer.dto.CustomerDetailResponse;
import com.instantops.customer.dto.CustomerResponse;

public interface CustomerService {

    PageResponse<CustomerResponse> getCustomers(int page, int size, String search);

    CustomerDetailResponse getCustomerById(Long id);
}
