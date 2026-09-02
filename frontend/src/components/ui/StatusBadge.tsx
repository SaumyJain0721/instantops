import { cn, BOOKING_STATUS_CONFIG, MECHANIC_STATUS_CONFIG } from '@/lib/utils'
import type { BookingStatus, MechanicStatus } from '@/api/types'

export function BookingStatusBadge({ status }: { status: BookingStatus }) {
  const config = BOOKING_STATUS_CONFIG[status]
  return (
    <span
      className={cn(
        'inline-flex items-center rounded-full px-2.5 py-0.5 text-xs font-semibold',
        config.bg,
        config.color,
      )}
    >
      {config.label}
    </span>
  )
}

export function MechanicStatusBadge({ status }: { status: MechanicStatus }) {
  const config = MECHANIC_STATUS_CONFIG[status]
  return (
    <span className={cn('inline-flex items-center gap-1.5 text-xs font-medium', config.color)}>
      <span className={cn('h-1.5 w-1.5 rounded-full', config.dot)} />
      {config.label}
    </span>
  )
}
