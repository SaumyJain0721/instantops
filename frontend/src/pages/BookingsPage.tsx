import { useState, useEffect } from 'react'
import { Search, SlidersHorizontal, ChevronUp, ChevronDown } from '@/icons'
import { useBookings, useBookingDetail } from '@/hooks/useBookings'
import { fetchServices } from '@/api/services'
import { fetchMechanics } from '@/api/mechanics'
import { BookingStatusBadge } from '@/components/ui/StatusBadge'
import { TableSkeleton } from '@/components/ui/LoadingSpinner'
import { ErrorState, EmptyState } from '@/components/ui/States'
import { Pagination } from '@/components/ui/Pagination'
import type { BookingStatus, ServiceSummary, MechanicSummary } from '@/api/types'
import { formatCurrency, formatDateTime } from '@/lib/utils'
import { BookingDetailDrawer } from '@/components/bookings/BookingDetailDrawer'

const ALL_STATUSES: BookingStatus[] = [
  'PENDING', 'ASSIGNED', 'ON_THE_WAY', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED',
]

export function BookingsPage({ liveEventKey }: { liveEventKey?: string }) {
  const { data, loading, error, filters, updateFilters, refresh } = useBookings()
  const [searchInput, setSearchInput] = useState('')
  const [services, setServices] = useState<ServiceSummary[]>([])
  const [mechanics, setMechanics] = useState<MechanicSummary[]>([])
  const [selectedBookingId, setSelectedBookingId] = useState<number | null>(null)
  const { data: bookingDetail, loading: detailLoading, mutateStatus } = useBookingDetail(selectedBookingId)

  // Load filter options
  useEffect(() => {
    fetchServices().then(setServices).catch(() => setServices([]))
    fetchMechanics().then(setMechanics).catch(() => setMechanics([]))
  }, [])

  // Debounce search
  useEffect(() => {
    const t = setTimeout(() => updateFilters({ search: searchInput }), 350)
    return () => clearTimeout(t)
  }, [searchInput]) // eslint-disable-line react-hooks/exhaustive-deps

  useEffect(() => {
    if (liveEventKey) void refresh()
  }, [liveEventKey]) // eslint-disable-line react-hooks/exhaustive-deps

  const handleSort = (col: string) => {
    if (filters.sortBy === col) {
      updateFilters({ sortDir: filters.sortDir === 'asc' ? 'desc' : 'asc' })
    } else {
      updateFilters({ sortBy: col, sortDir: 'desc' })
    }
  }

  const SortIcon = ({ col }: { col: string }) => {
    if (filters.sortBy !== col) return <ChevronUp className="h-3 w-3 opacity-30" />
    return filters.sortDir === 'asc'
      ? <ChevronUp className="h-3 w-3 text-indigo-600" />
      : <ChevronDown className="h-3 w-3 text-indigo-600" />
  }

  return (
    <div className="space-y-4">
      {/* Filters */}
      <div className="rounded-xl border border-slate-200 bg-white p-4 shadow-sm">
        <div className="flex flex-wrap items-center gap-3">
          {/* Search */}
          <div className="relative flex-1 min-w-48">
            <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-slate-400" />
            <input
              value={searchInput}
              onChange={e => setSearchInput(e.target.value)}
              placeholder="Search by name, plate, booking no…"
              className="w-full rounded-lg border border-slate-200 bg-slate-50 py-2 pl-9 pr-3 text-sm text-slate-900 placeholder:text-slate-400 focus:border-indigo-400 focus:outline-none focus:ring-1 focus:ring-indigo-100"
            />
          </div>

          {/* Status */}
          <select
            value={filters.status ?? ''}
            onChange={e => updateFilters({ status: (e.target.value as BookingStatus) || '' })}
            className="rounded-lg border border-slate-200 bg-slate-50 px-3 py-2 text-sm text-slate-700 focus:outline-none"
          >
            <option value="">All Statuses</option>
            {ALL_STATUSES.map(s => (
              <option key={s} value={s}>{s.replace('_', ' ')}</option>
            ))}
          </select>

          {/* Service */}
          <select
            value={filters.serviceId ?? ''}
            onChange={e => updateFilters({ serviceId: e.target.value ? Number(e.target.value) : '' })}
            className="rounded-lg border border-slate-200 bg-slate-50 px-3 py-2 text-sm text-slate-700 focus:outline-none"
          >
            <option value="">All Services</option>
            {services.map(s => <option key={s.id} value={s.id}>{s.name}</option>)}
          </select>

          {/* Mechanic */}
          <select
            value={filters.mechanicId ?? ''}
            onChange={e => updateFilters({ mechanicId: e.target.value ? Number(e.target.value) : '' })}
            className="rounded-lg border border-slate-200 bg-slate-50 px-3 py-2 text-sm text-slate-700 focus:outline-none"
          >
            <option value="">All Mechanics</option>
            {mechanics.map(m => <option key={m.id} value={m.id}>{m.name}</option>)}
          </select>

          {/* Size */}
          <div className="flex items-center gap-1.5 text-sm text-slate-500">
            <SlidersHorizontal className="h-4 w-4" />
            <select
              value={filters.size ?? 12}
              onChange={e => updateFilters({ size: Number(e.target.value) })}
              className="rounded border border-slate-200 bg-slate-50 px-2 py-1.5 text-sm text-slate-700 focus:outline-none"
            >
              {[10, 12, 20, 50].map(n => <option key={n} value={n}>{n} / page</option>)}
            </select>
          </div>
        </div>
      </div>

      {/* Table */}
      {error ? (
        <ErrorState message={error} onRetry={refresh} />
      ) : loading ? (
        <TableSkeleton rows={10} cols={8} />
      ) : (
        <div className="overflow-hidden rounded-xl border border-slate-200 bg-white shadow-sm">
          <div className="overflow-x-auto">
            <table className="min-w-full text-sm">
              <thead>
                <tr className="border-b border-slate-100 bg-slate-50">
                  {[
                    { label: 'Booking', col: 'bookingNumber' },
                    { label: 'Customer', col: null },
                    { label: 'Vehicle', col: null },
                    { label: 'Service', col: null },
                    { label: 'Mechanic', col: null },
                    { label: 'Status', col: 'status' },
                    { label: 'Scheduled', col: 'scheduledAt' },
                    { label: 'Amount', col: 'totalAmount' },
                  ].map(({ label, col }) => (
                    <th
                      key={label}
                      onClick={() => col && handleSort(col)}
                      className={`px-4 py-3 text-left text-xs font-semibold uppercase tracking-wider text-slate-500 ${col ? 'cursor-pointer select-none hover:text-slate-900' : ''}`}
                    >
                      <span className="inline-flex items-center gap-1">
                        {label}
                        {col && <SortIcon col={col} />}
                      </span>
                    </th>
                  ))}
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100">
                {data?.content.map(b => (
                  <tr
                    key={b.id}
                    className="hover:bg-slate-50 cursor-pointer transition-colors"
                    onClick={() => setSelectedBookingId(b.id)}
                  >
                    <td className="px-4 py-3.5">
                      <span className="font-mono text-xs font-semibold text-indigo-600">
                        {b.bookingNumber}
                      </span>
                    </td>
                    <td className="px-4 py-3.5">
                      <div className="font-medium text-slate-900">{b.customerName}</div>
                      <div className="text-xs text-slate-400">{b.customerPhone}</div>
                    </td>
                    <td className="px-4 py-3.5">
                      <div className="text-slate-800">{b.vehicleInfo}</div>
                      <div className="text-xs font-mono text-slate-400">{b.licensePlate}</div>
                    </td>
                    <td className="px-4 py-3.5 text-slate-600">{b.serviceName}</td>
                    <td className="px-4 py-3.5">
                      {b.mechanicName ? (
                        <div>
                          <div className="text-slate-800">{b.mechanicName}</div>
                          {b.mechanicSpecialization && <div className="text-xs text-slate-400">{b.mechanicSpecialization}</div>}
                        </div>
                      ) : (
                        <span className="text-slate-400">Unassigned</span>
                      )}
                    </td>
                    <td className="px-4 py-3.5">
                      <BookingStatusBadge status={b.status} />
                    </td>
                    <td className="px-4 py-3.5 text-slate-500 text-xs whitespace-nowrap">
                      {formatDateTime(b.scheduledAt)}
                    </td>
                    <td className="px-4 py-3.5 font-semibold text-slate-900">
                      {formatCurrency(b.totalAmount)}
                    </td>
                  </tr>
                ))}
                {data?.content.length === 0 && (
                  <tr>
                    <td colSpan={8}>
                      <EmptyState title="No bookings found" description="Try adjusting your filters." />
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>

          {data && data.totalPages > 0 && (
            <div className="border-t border-slate-100 px-4 py-3">
              <Pagination
                page={data.page}
                totalPages={data.totalPages}
                totalElements={data.totalElements}
                size={data.size}
                onPageChange={p => updateFilters({ page: p })}
              />
            </div>
          )}
        </div>
      )}

      {/* Detail Drawer */}
      <BookingDetailDrawer
        booking={bookingDetail}
        loading={detailLoading}
        open={selectedBookingId !== null}
        onClose={() => setSelectedBookingId(null)}
        onStatusUpdated={refresh}
        onStatusChange={mutateStatus}
      />
    </div>
  )
}
