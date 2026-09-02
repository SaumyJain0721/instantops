import { useEffect, useState, } from 'react'
import { X, Wrench, Car, User, Phone, Calendar, DollarSign, FileText, CheckCircle2 } from '@/icons'
import type { BookingDetail, BookingStatus } from '@/api/types'
import { BookingStatusBadge } from '@/components/ui/StatusBadge'
import { LoadingSpinner } from '@/components/ui/LoadingSpinner'
import { formatCurrency, formatDateTime } from '@/lib/utils'

interface BookingDetailDrawerProps {
  open: boolean
  onClose: () => void
  booking: BookingDetail | null
  loading: boolean
  onStatusUpdated?: () => void
  onStatusChange?: (status: string, mechanicId?: number, notes?: string) => Promise<BookingDetail | undefined>
}

export function BookingDetailDrawer({
  open,
  onClose,
  booking,
  loading,
  onStatusUpdated,
  onStatusChange,
}: BookingDetailDrawerProps) {
  const [nextStatus, setNextStatus] = useState<BookingStatus | ''>('')
  const [updating, setUpdating] = useState(false)
  const [updateError, setUpdateError] = useState<string | null>(null)

  useEffect(() => {
    setNextStatus('')
    setUpdateError(null)
  }, [booking?.id, booking?.status, open])

  if (!open) return null

  return (
    <>
      {/* Backdrop */}
      <div
        className="fixed inset-0 z-40 bg-black/20 backdrop-blur-sm"
        onClick={onClose}
      />

      {/* Drawer */}
      <div className="fixed right-0 top-0 z-50 flex h-full w-full max-w-md flex-col bg-white shadow-xl">
        {/* Header */}
        <div className="flex items-center justify-between border-b border-slate-200 px-5 py-4">
          <div>
            <p className="text-xs text-slate-500">Booking Detail</p>
            <p className="font-semibold text-slate-900">
              {booking ? booking.bookingNumber : '—'}
            </p>
          </div>
          <button
            onClick={onClose}
            className="flex h-8 w-8 items-center justify-center rounded-lg border border-slate-200 text-slate-400 hover:bg-slate-50 hover:text-slate-600 transition-colors"
          >
            <X className="h-4 w-4" />
          </button>
        </div>

        {/* Body */}
        <div className="flex-1 overflow-y-auto p-5">
          {loading ? (
            <LoadingSpinner text="Loading booking..." />
          ) : booking ? (
            <div className="space-y-5">
              {/* Status */}
              <div className="flex items-center gap-3">
                <BookingStatusBadge status={booking.status} />
                <span className="text-xs text-slate-400">
                  Created {formatDateTime(booking.createdAt)}
                </span>
              </div>

              {onStatusChange && booking.status !== 'COMPLETED' && booking.status !== 'CANCELLED' && (
                <div className="rounded-lg border border-indigo-100 bg-indigo-50/50 p-4">
                  <div className="mb-2 flex items-center gap-1.5 text-xs font-semibold uppercase tracking-wider text-indigo-700">
                    <CheckCircle2 className="h-4 w-4" />
                    Update status
                  </div>
                  <div className="flex gap-2">
                    <select
                      value={nextStatus}
                      onChange={(e) => setNextStatus(e.target.value as BookingStatus)}
                      className="min-w-0 flex-1 rounded-lg border border-slate-200 bg-white px-3 py-2 text-sm text-slate-700 focus:outline-none focus:ring-2 focus:ring-indigo-100"
                    >
                      <option value="">Select next status</option>
                      {getAllowedNextStatuses(booking.status).map(status => (
                        <option key={status} value={status}>
                          {status.replace(/_/g, " ")}
                        </option>
                      ))}
                    </select>
                    <button
                      type="button"
                      disabled={!nextStatus || updating}
                      onClick={async () => {
                        if (!nextStatus || !onStatusChange) return
                        try {
                          setUpdating(true)
                          setUpdateError(null)
                          await onStatusChange(nextStatus)
                          onStatusUpdated?.()
                          setNextStatus('')
                        } catch (e) {
                          setUpdateError(e instanceof Error ? e.message : 'Failed to update status')
                        } finally {
                          setUpdating(false)
                        }
                      }}
                      className="shrink-0 rounded-lg bg-indigo-600 px-3 py-2 text-sm font-medium text-white transition hover:bg-indigo-700 disabled:cursor-not-allowed disabled:opacity-50"
                    >
                      {updating ? 'Updating…' : 'Update'}
                    </button>
                  </div>
                  {updateError && <p className="mt-2 text-xs text-rose-600">{updateError}</p>}
                  <p className="mt-2 text-[11px] text-slate-500">Status changes are broadcast to connected dashboards in real time.</p>
                </div>
              )}

              {/* Customer */}
              <Section icon={<User className="h-4 w-4" />} title="Customer">
                <p className="font-medium text-slate-900">{booking.customer?.name}</p>
                <p className="flex items-center gap-1 text-sm text-slate-500">
                  <Phone className="h-3.5 w-3.5" />
                  {booking.customer?.phone}
                </p>
              </Section>

              {/* Vehicle */}
              <Section icon={<Car className="h-4 w-4" />} title="Vehicle">
                <p className="font-medium text-slate-900">{booking.vehicle ? `${booking.vehicle.year} ${booking.vehicle.make} ${booking.vehicle.model}` : '—'}</p>
                <p className="font-mono text-sm text-slate-500">{booking.vehicle?.licensePlate}</p>
              </Section>

              {/* Service */}
              <Section icon={<Wrench className="h-4 w-4" />} title="Service">
                <p className="font-medium text-slate-900">{booking.service?.name}</p>
              </Section>

              {/* Mechanic */}
              {booking.mechanic?.name && (
                <Section icon={<Wrench className="h-4 w-4 text-indigo-500" />} title="Mechanic">
                  <p className="font-medium text-slate-900">{booking.mechanic?.name}</p>
                </Section>
              )}

              {/* Schedule */}
              <Section icon={<Calendar className="h-4 w-4" />} title="Scheduled">
                <p className="font-medium text-slate-900">
                  {formatDateTime(booking.scheduledAt)}
                </p>
                {booking.completedAt && (
                  <p className="text-sm text-slate-500">
                    Completed: {formatDateTime(booking.completedAt)}
                  </p>
                )}
              </Section>

              {/* Amount */}
              <Section icon={<DollarSign className="h-4 w-4" />} title="Amount">
                <p className="text-xl font-bold text-slate-900">
                  {formatCurrency(booking.totalAmount)}
                </p>
              </Section>

              {/* Notes */}
              {booking.notes && (
                <Section icon={<FileText className="h-4 w-4" />} title="Notes">
                  <p className="text-sm text-slate-600 whitespace-pre-wrap">{booking.notes}</p>
                </Section>
              )}
            </div>
          ) : (
            <p className="text-center text-sm text-slate-500 py-10">No booking selected.</p>
          )}
        </div>
      </div>
    </>
  )
}


function getAllowedNextStatuses(status: BookingStatus): BookingStatus[] {
  switch (status) {
    case 'PENDING':
      return ['ASSIGNED', 'ON_THE_WAY', 'IN_PROGRESS', 'CANCELLED']
    case 'ASSIGNED':
      return ['ON_THE_WAY', 'IN_PROGRESS', 'PENDING', 'CANCELLED']
    case 'ON_THE_WAY':
      return ['IN_PROGRESS', 'ASSIGNED', 'CANCELLED']
    case 'IN_PROGRESS':
      return ['COMPLETED', 'CANCELLED', 'ON_THE_WAY']
    default:
      return []
  }
}

function Section({
  icon,
  title,
  children,
}: {
  icon: React.ReactNode
  title: string
  children: React.ReactNode
}) {
  return (
    <div className="rounded-lg border border-slate-100 bg-slate-50 p-4">
      <div className="mb-2 flex items-center gap-1.5 text-xs font-semibold uppercase tracking-wider text-slate-500">
        {icon}
        {title}
      </div>
      <div className="space-y-0.5">{children}</div>
    </div>
  )
}
