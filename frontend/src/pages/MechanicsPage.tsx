import { useEffect } from 'react'
import { Wrench, Phone, Activity, CalendarCheck2, CheckCircle2 } from '@/icons'
import { useMechanics } from '@/hooks/useMechanics'
import { MechanicStatusBadge } from '@/components/ui/StatusBadge'
import { TableSkeleton } from '@/components/ui/LoadingSpinner'
import { ErrorState, EmptyState } from '@/components/ui/States'
import { formatDateTime } from '@/lib/utils'

export function MechanicsPage({ liveEventKey }: { liveEventKey?: string }) {
 const {data,loading,error,refresh}=useMechanics()
 useEffect(() => { if (liveEventKey) void refresh() }, [liveEventKey])
 if(error) return <ErrorState message={error} onRetry={refresh}/>
 return <div className="space-y-4">
  {loading ? <TableSkeleton rows={12} cols={5}/> : <div className="overflow-hidden rounded-xl border border-slate-200 bg-white shadow-sm">
   <div className="flex items-center justify-between border-b border-slate-100 px-5 py-4"><div><h2 className="text-sm font-semibold text-slate-900">Mechanic Operations</h2><p className="text-xs text-slate-500 mt-0.5">Current field-team availability and workload</p></div><span className="rounded-full bg-slate-100 px-2.5 py-1 text-xs font-medium text-slate-600">{data?.length ?? 0} mechanics</span></div>
   <div className="grid gap-3 p-4 md:grid-cols-2 xl:grid-cols-3">
    {data?.map(m=><div key={m.id} className="rounded-xl border border-slate-200 p-4 transition hover:border-indigo-200 hover:shadow-sm">
      <div className="flex items-start justify-between gap-3"><div className="flex items-center gap-3"><div className="flex h-10 w-10 items-center justify-center rounded-full bg-indigo-50 text-indigo-600"><Wrench className="h-4 w-4"/></div><div><p className="font-semibold text-slate-900">{m.name}</p><p className="text-xs text-slate-500">{m.specialization}</p></div></div><MechanicStatusBadge status={m.status}/></div>
      <div className="mt-4 grid grid-cols-2 gap-2"><div className="rounded-lg bg-slate-50 p-3"><p className="text-[11px] text-slate-400">Active jobs</p><p className="mt-1 flex items-center gap-1 font-semibold text-slate-900"><Activity className="h-3.5 w-3.5 text-indigo-500"/>{m.activeBookingsCount}</p></div><div className="rounded-lg bg-slate-50 p-3"><p className="text-[11px] text-slate-400">Jobs completed</p><p className="mt-1 flex items-center gap-1 font-semibold text-slate-900"><CheckCircle2 className="h-3.5 w-3.5 text-emerald-500"/>{m.jobsCompleted}</p></div></div>
      <div className="mt-2 rounded-lg bg-slate-50 p-3"><p className="text-[11px] text-slate-400">{m.currentBooking ? 'Current booking' : 'Last booking'}</p>{(m.currentBooking ?? m.lastBooking) ? <><p className="mt-1 font-mono text-xs font-semibold text-indigo-600">{(m.currentBooking ?? m.lastBooking)?.bookingNumber}</p><p className="text-xs text-slate-600">{(m.currentBooking ?? m.lastBooking)?.customerName} · {(m.currentBooking ?? m.lastBooking)?.serviceName}</p><p className="mt-0.5 flex items-center gap-1 text-[11px] text-slate-400"><CalendarCheck2 className="h-3 w-3"/>{formatDateTime((m.currentBooking ?? m.lastBooking)!.scheduledAt)}</p></> : <p className="mt-1 text-xs text-slate-400">No bookings yet</p>}</div>
      <div className="mt-2 rounded-lg bg-slate-50 p-3"><p className="text-[11px] text-slate-400">Phone</p><a href={`tel:${m.phone}`} className="mt-1 flex items-center gap-1 truncate text-xs font-medium text-slate-700 hover:text-indigo-600"><Phone className="h-3.5 w-3.5"/>{m.phone}</a></div>
    </div>)}
    {data?.length===0 && <EmptyState title="No mechanics found"/>}
   </div>
  </div>}
 </div>
}
