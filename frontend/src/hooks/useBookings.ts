import { useCallback, useEffect, useState } from 'react'
import { fetchBookingById, fetchBookings, updateBookingStatus } from '@/api/bookings'
import type { BookingDetail, BookingFilters, BookingSummary, PageResponse } from '@/api/types'

export function useBookings(initialFilters: BookingFilters = {}) {
  const [data, setData] = useState<PageResponse<BookingSummary> | null>(null)
  const [filters, setFilters] = useState<BookingFilters>({
    page: 0,
    size: 12,
    sortBy: 'createdAt',
    sortDir: 'desc',
    ...initialFilters,
  })
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const load = useCallback(async () => {
    try {
      setLoading(true)
      setError(null)
      const d = await fetchBookings(filters)
      setData(d)
    } catch (e) {
      setError(e instanceof Error ? e.message : 'Failed to load bookings')
    } finally {
      setLoading(false)
    }
  }, [filters])

  useEffect(() => { void load() }, [load])

  const updateFilters = (patch: Partial<BookingFilters>) => {
    setFilters(prev => ({ ...prev, ...patch, page: patch.page ?? 0 }))
  }

  return { data, loading, error, filters, updateFilters, refresh: load }
}

export function useBookingDetail(id: number | null) {
  const [data, setData] = useState<BookingDetail | null>(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    if (!id) { setData(null); return }
    setLoading(true)
    setError(null)
    fetchBookingById(id)
      .then(setData)
      .catch(e => setError(e instanceof Error ? e.message : 'Failed to load booking'))
      .finally(() => setLoading(false))
  }, [id])

  const mutateStatus = async (status: string, mechanicId?: number, notes?: string) => {
    if (!id) return
    const updated = await updateBookingStatus(id, status, mechanicId, notes)
    setData(updated)
    return updated
  }

  return { data, loading, error, mutateStatus }
}
