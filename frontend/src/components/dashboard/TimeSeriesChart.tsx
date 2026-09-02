import type { TimeSeriesPoint } from '@/api/types'
import { formatCurrency } from '@/lib/utils'

interface Props { data: TimeSeriesPoint[] }
function dateLabel(s: string) { return new Date(s).toLocaleDateString('en-IN', { month: 'short', day: 'numeric' }) }
function Chart({ data, metric, title, subtitle }: Props & { metric: 'count'|'revenue'; title: string; subtitle: string }) {
  const values = data.map(d => metric === 'count' ? d.count : Number(d.revenue ?? 0))
  const max = Math.max(...values, 1)
  const w = 700, h = 210, px = 12, py = 18
  const points = data.map((d,i) => {
    const x = px + (i * (w - px*2) / Math.max(data.length-1,1))
    const y = h-py - ((metric === 'count' ? d.count : Number(d.revenue ?? 0))/max)*(h-py*2)
    return `${x},${y}`
  }).join(' ')
  return <div className="rounded-xl border border-slate-200 bg-white p-5 shadow-sm">
    <div className="mb-4"><h3 className="text-sm font-semibold text-slate-900">{title}</h3><p className="mt-0.5 text-xs text-slate-500">{subtitle}</p></div>
    <svg viewBox={`0 0 ${w} ${h}`} className="h-52 w-full" role="img" aria-label={title}>
      {[0,1,2,3].map(i => <line key={i} x1={px} x2={w-px} y1={py+i*(h-py*2)/3} y2={py+i*(h-py*2)/3} stroke="#e2e8f0" strokeDasharray="4 5" />)}
      {points && <polyline fill="none" stroke={metric==='count' ? '#4f46e5' : '#059669'} strokeWidth="3" strokeLinejoin="round" strokeLinecap="round" points={points} />}
      {data.map((d,i) => { const x=px+(i*(w-px*2)/Math.max(data.length-1,1)); const y=h-py-(values[i]/max)*(h-py*2); return <g key={d.date}><circle cx={x} cy={y} r="3.5" fill={metric==='count'?'#4f46e5':'#059669'} /><text x={x} y={h-2} textAnchor="middle" fontSize="10" fill="#94a3b8">{dateLabel(d.date)}</text></g> })}
    </svg>
    <div className="mt-1 flex justify-between text-xs text-slate-400"><span>Peak {metric==='count' ? Math.max(...values,0) : formatCurrency(Math.max(...values,0))}</span><span>{data.length} days</span></div>
  </div>
}
export function BookingsTimeSeriesChart({ data }: Props) { return <Chart data={data} metric="count" title="Bookings Over Time" subtitle="Last 14 days" /> }
export function RevenueTimeSeriesChart({ data }: Props) { return <Chart data={data} metric="revenue" title="Revenue Over Time" subtitle="Last 14 days" /> }
