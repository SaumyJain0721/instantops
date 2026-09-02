import {
  CalendarCheck2,
  CheckCircle,
  Clock,
  DollarSign,
  TrendingUp,
  Users,
  Wrench,
  XCircle,
} from '@/icons'
import { useDashboard } from '@/hooks/useDashboard'
import { KpiCard } from '@/components/ui/KpiCard'
import { CardSkeleton, ChartSkeleton } from '@/components/ui/LoadingSpinner'
import { ErrorState } from '@/components/ui/States'
import { PipelineBar } from '@/components/dashboard/PipelineBar'
import {
  BookingsTimeSeriesChart,
  RevenueTimeSeriesChart,
} from '@/components/dashboard/TimeSeriesChart'
import { StatusDonut } from '@/components/dashboard/StatusDonut'
import { ServiceBreakdown } from '@/components/dashboard/ServiceBreakdown'
import { RecentActivity } from '@/components/dashboard/RecentActivity'
import { formatCurrency } from '@/lib/utils'
import { useEffect } from 'react'

export function DashboardPage({ liveEventKey }: { liveEventKey?: string }) {
  const { data, loading, error, refresh } = useDashboard()

  useEffect(() => {
    if (liveEventKey) void refresh()
  }, [liveEventKey])

  if (error) {
    return <ErrorState message={error} onRetry={refresh} />
  }

  return (
    <div className="space-y-6">
      {/* KPI Cards */}
      <div className="grid grid-cols-2 gap-4 lg:grid-cols-4">
        {loading ? (
          Array.from({ length: 8 }).map((_, i) => <CardSkeleton key={i} />)
        ) : data ? (
          <>
            <KpiCard
              title="Total Bookings"
              value={data.summary.totalBookings.toLocaleString('en-IN')}
              subtitle="All time"
              icon={<CalendarCheck2 className="h-5 w-5 text-indigo-600" />}
              iconBg="bg-indigo-50"
            />
            <KpiCard
              title="Today's Bookings"
              value={data.summary.todayBookings}
              subtitle="Scheduled today"
              icon={<Clock className="h-5 w-5 text-amber-600" />}
              iconBg="bg-amber-50"
            />
            <KpiCard
              title="Completed"
              value={data.summary.completedBookings.toLocaleString('en-IN')}
              subtitle="Successfully closed"
              icon={<CheckCircle className="h-5 w-5 text-emerald-600" />}
              iconBg="bg-emerald-50"
            />
            <KpiCard
              title="Pending"
              value={data.summary.pendingBookings}
              subtitle="Awaiting assignment"
              icon={<TrendingUp className="h-5 w-5 text-blue-600" />}
              iconBg="bg-blue-50"
            />
            <KpiCard
              title="Total Revenue"
              value={formatCurrency(data.summary.totalRevenue)}
              subtitle="All completed bookings"
              icon={<DollarSign className="h-5 w-5 text-emerald-600" />}
              iconBg="bg-emerald-50"
            />
            <KpiCard
              title="Cancelled"
              value={data.summary.cancelledBookings}
              subtitle="Cancellation rate"
              icon={<XCircle className="h-5 w-5 text-rose-500" />}
              iconBg="bg-rose-50"
            />
            <KpiCard
              title="Active Mechanics"
              value={data.summary.activeMechanics}
              subtitle="Available + On duty + Busy"
              icon={<Wrench className="h-5 w-5 text-violet-600" />}
              iconBg="bg-violet-50"
            />
            <KpiCard
              title="New Customers"
              value={data.summary.newCustomers}
              subtitle="This month"
              icon={<Users className="h-5 w-5 text-sky-600" />}
              iconBg="bg-sky-50"
            />
          </>
        ) : null}
      </div>

      {/* Pipeline */}
      {loading ? (
        <ChartSkeleton />
      ) : data ? (
        <PipelineBar pulse={data.operationsPulse} />
      ) : null}

      {/* Time series charts side by side */}
      <div className="grid grid-cols-1 gap-4 lg:grid-cols-2">
        {loading ? (
          <>
            <ChartSkeleton />
            <ChartSkeleton />
          </>
        ) : data ? (
          <>
            <BookingsTimeSeriesChart data={data.bookingsOverTime} />
            <RevenueTimeSeriesChart data={data.revenueOverTime} />
          </>
        ) : null}
      </div>

      {/* Status donut + Service breakdown */}
      <div className="grid grid-cols-1 gap-4 lg:grid-cols-2">
        {loading ? (
          <>
            <ChartSkeleton />
            <ChartSkeleton />
          </>
        ) : data ? (
          <>
            <StatusDonut data={data.statusDistribution} />
            <ServiceBreakdown data={data.serviceBreakdown} />
          </>
        ) : null}
      </div>

      {/* Recent activity */}
      {loading ? (
        <ChartSkeleton />
      ) : data ? (
        <RecentActivity items={data.recentActivity} />
      ) : null}
    </div>
  )
}
