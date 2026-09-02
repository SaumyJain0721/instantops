import { BOOKING_STATUS_CONFIG } from '@/lib/utils'
import type { OperationsPulse } from '@/api/types'
import type { BookingStatus } from '@/api/types'

const PIPELINE_STAGES: BookingStatus[] = [
  'PENDING',
  'ASSIGNED',
  'ON_THE_WAY',
  'IN_PROGRESS',
  'COMPLETED',
]

interface PipelineBarProps {
  pulse: OperationsPulse
}

export function PipelineBar({ pulse }: PipelineBarProps) {
  const total = Object.values(pulse).reduce((s, v) => s + v, 0)

  return (
    <div className="rounded-xl border border-slate-200 bg-white p-5 shadow-sm">
      <div className="mb-4">
        <h3 className="text-sm font-semibold text-slate-900">Operations Pipeline</h3>
        <p className="text-xs text-slate-500 mt-0.5">{total} total bookings across all stages</p>
      </div>

      {/* Stacked bar */}
      <div className="flex h-3 overflow-hidden rounded-full bg-slate-100">
        {PIPELINE_STAGES.map((status) => {
          const count = pulse[status === 'PENDING' ? 'pending' : status === 'ASSIGNED' ? 'assigned' : status === 'ON_THE_WAY' ? 'onTheWay' : status === 'IN_PROGRESS' ? 'inProgress' : status === 'COMPLETED' ? 'completed' : 'cancelled'] ?? 0
          const pct = total > 0 ? (count / total) * 100 : 0
          const cfg = BOOKING_STATUS_CONFIG[status]
          return (
            <div
              key={status}
              className="h-full transition-all"
              style={{ width: `${pct}%`, backgroundColor: statusBarColor(status) }}
              title={`${cfg.label}: ${count}`}
            />
          )
        })}
      </div>

      {/* Legend */}
      <div className="mt-4 grid grid-cols-2 gap-x-6 gap-y-2.5 sm:grid-cols-3">
        {(([['PENDING',pulse.pending],['ASSIGNED',pulse.assigned],['ON_THE_WAY',pulse.onTheWay],['IN_PROGRESS',pulse.inProgress],['COMPLETED',pulse.completed],['CANCELLED',pulse.cancelled]] as [BookingStatus, number][])).map(([status, count]) => {
          const cfg = BOOKING_STATUS_CONFIG[status]
          if (!cfg) return null
          const pct = total > 0 ? Math.round((count / total) * 100) : 0
          return (
            <div key={status} className="flex items-center justify-between gap-2">
              <div className="flex items-center gap-2 min-w-0">
                <span
                  className="h-2.5 w-2.5 shrink-0 rounded-sm"
                  style={{ backgroundColor: statusBarColor(status) }}
                />
                <span className="truncate text-xs text-slate-600">{cfg.label}</span>
              </div>
              <div className="flex items-center gap-1.5 shrink-0">
                <span className="text-xs font-semibold text-slate-900">{count}</span>
                <span className="text-[10px] text-slate-400">({pct}%)</span>
              </div>
            </div>
          )
        })}
      </div>
    </div>
  )
}

function statusBarColor(status: BookingStatus): string {
  const map: Record<BookingStatus, string> = {
    PENDING: '#F59E0B',
    ASSIGNED: '#3B82F6',
    ON_THE_WAY: '#8B5CF6',
    IN_PROGRESS: '#6366F1',
    COMPLETED: '#10B981',
    CANCELLED: '#F43F5E',
  }
  return map[status] ?? '#94A3B8'
}
