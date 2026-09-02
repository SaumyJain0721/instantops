import { get } from './client'
import type { CustomerDetail, CustomerSummary, PageResponse } from './types'

export async function fetchCustomers(
  page = 0,
  size = 20,
  search = '',
): Promise<PageResponse<CustomerSummary>> {
  const params = new URLSearchParams({ page: String(page), size: String(size) })
  if (search) params.set('search', search)
  return get<PageResponse<CustomerSummary>>(`/customers?${params}`)
}

export async function fetchCustomerById(id: number): Promise<CustomerDetail> {
  return get<CustomerDetail>(`/customers/${id}`)
}
