import type { RecentActivityItem } from '@/api/types'
import { BookingStatusBadge } from '@/components/ui/StatusBadge'
import { formatCurrency, formatRelative } from '@/lib/utils'
import { Car, Wrench } from '@/icons'

interface RecentActivityProps {
  items: RecentActivityItem[]
}

export function RecentActivity({ items }: RecentActivityProps) {
  return (
    <div className="rounded-xl border border-slate-200 bg-white shadow-sm">
      <div className="border-b border-slate-100 px-5 py-4">
        <h3 className="text-sm font-semibold text-slate-900">Recent Activity</h3>
        <p className="text-xs text-slate-500 mt-0.5">Latest booking events</p>
      </div>
      <div className="divide-y divide-slate-100">
        {items.slice(0, 10).map((item) => (
          <div key={item.id} className="flex items-start gap-4 px-5 py-3.5 hover:bg-slate-50 transition-colors">
            {/* Icon */}
            <div className="flex h-8 w-8 shrink-0 items-center justify-center rounded-full bg-indigo-50 text-indigo-500 mt-0.5">
              <Car className="h-4 w-4" />
            </div>

            {/* Info */}
            <div className="flex-1 min-w-0">
              <div className="flex items-center gap-2 flex-wrap">
                <span className="text-xs font-semibold text-slate-900">{item.bookingNumber}</span>
                <BookingStatusBadge status={item.status} />
              </div>
              <p className="mt-0.5 text-xs text-slate-600 truncate">
                {item.customerName} · {item.vehicleInfo}
              </p>
              <div className="mt-0.5 flex items-center gap-2 text-[11px] text-slate-400">
                <span className="flex items-center gap-1">
                  <Wrench className="h-3 w-3" />
                  {item.serviceName}
                </span>
                <span>·</span>
                <span>{formatCurrency(item.totalAmount)}</span>
              </div>
            </div>

            {/* Time */}
            <span className="shrink-0 text-[11px] text-slate-400 pt-0.5">
              {formatRelative(item.createdAt)}
            </span>
          </div>
        ))}

        {items.length === 0 && (
          <div className="px-5 py-8 text-center text-sm text-slate-500">
            No recent activity
          </div>
        )}
      </div>
    </div>
  )
}
