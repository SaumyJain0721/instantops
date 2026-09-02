import { get, patch } from './client'
import type {
  BookingDetail,
  BookingFilters,
  BookingSummary,
  PageResponse,
} from './types'

function buildQuery(filters: BookingFilters): string {
  const params = new URLSearchParams()
  if (filters.page !== undefined) params.set('page', String(filters.page))
  if (filters.size !== undefined) params.set('size', String(filters.size))
  if (filters.search) params.set('search', filters.search)
  if (filters.status) params.set('status', filters.status)
  if (filters.mechanicId) params.set('mechanicId', String(filters.mechanicId))
  if (filters.serviceId) params.set('serviceId', String(filters.serviceId))
  if (filters.sortBy) params.set('sortBy', filters.sortBy)
  if (filters.sortDir) params.set('sortDir', filters.sortDir)
  const qs = params.toString()
  return qs ? `?${qs}` : ''
}

export async function fetchBookings(
  filters: BookingFilters,
): Promise<PageResponse<BookingSummary>> {
  return get<PageResponse<BookingSummary>>(`/bookings${buildQuery(filters)}`)
}

export async function fetchBookingById(id: number): Promise<BookingDetail> {
  return get<BookingDetail>(`/bookings/${id}`)
}

export async function updateBookingStatus(
  id: number,
  status: string,
  mechanicId?: number,
  notes?: string,
): Promise<BookingDetail> {
  return patch<BookingDetail>(`/bookings/${id}/status`, {
    status,
    mechanicId: mechanicId ?? null,
    notes: notes ?? null,
  })
}
