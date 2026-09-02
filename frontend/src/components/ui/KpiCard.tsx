import type { ReactNode } from 'react'
import { cn } from '@/lib/utils'

interface KpiCardProps {
  title: string
  value: string | number
  subtitle?: string
  icon: ReactNode
  iconBg?: string
  trend?: { value: string; up: boolean }
  className?: string
}

export function KpiCard({
  title,
  value,
  subtitle,
  icon,
  iconBg = 'bg-slate-100',
  trend,
  className,
}: KpiCardProps) {
  return (
    <div className={cn('rounded-xl border border-slate-200 bg-white p-5 shadow-sm', className)}>
      <div className="flex items-start justify-between">
        <div>
          <p className="text-xs font-semibold uppercase tracking-wider text-slate-500">{title}</p>
          <p className="mt-2 text-3xl font-bold tracking-tight text-slate-900">{value}</p>
          {subtitle && (
            <p className="mt-1 text-sm text-slate-500">{subtitle}</p>
          )}
          {trend && (
            <span
              className={cn(
                'mt-2 inline-flex items-center text-xs font-medium',
                trend.up ? 'text-emerald-600' : 'text-rose-600',
              )}
            >
              {trend.up ? '↑' : '↓'} {trend.value}
            </span>
          )}
        </div>
        <div className={cn('flex h-10 w-10 items-center justify-center rounded-lg', iconBg)}>
          {icon}
        </div>
      </div>
    </div>
  )
}
