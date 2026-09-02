import type { ServiceBreakdownItem } from '@/api/types'
import { formatCurrency } from '@/lib/utils'
export function ServiceBreakdown({ data }: { data: ServiceBreakdownItem[] }) {
  const rows=data.slice(0,8), max=Math.max(...rows.map(x=>x.bookingCount),1)
  return <div className="rounded-xl border border-slate-200 bg-white p-5 shadow-sm"><div className="mb-4"><h3 className="text-sm font-semibold text-slate-900">Service Breakdown</h3><p className="mt-0.5 text-xs text-slate-500">Bookings per service type</p></div><div className="space-y-4">{rows.map(r=><div key={r.serviceName}><div className="mb-1.5 flex items-center justify-between gap-3 text-xs"><span className="truncate font-medium text-slate-700">{r.serviceName}</span><span className="shrink-0 text-slate-500">{r.bookingCount} · {formatCurrency(Number(r.revenue))}</span></div><div className="h-2 rounded-full bg-slate-100"><div className="h-2 rounded-full bg-indigo-500 transition-all" style={{width:`${r.bookingCount/max*100}%`}} /></div></div>)}</div></div>
}
