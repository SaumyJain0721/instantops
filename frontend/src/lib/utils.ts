export type ClassValue = string | false | null | undefined | Record<string, boolean> | ClassValue[]
function clsx(...inputs: ClassValue[]): string {
  return inputs.flatMap(v => Array.isArray(v) ? clsx(...v as ClassValue[]).split(' ').filter(Boolean) : typeof v === 'string' ? [v] : v && typeof v === 'object' ? Object.entries(v).filter(([, ok]) => ok).map(([k]) => k) : []).join(' ')
}
function twMerge(value: string): string { return value }
import type { BookingStatus, MechanicStatus } from '@/api/types'

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs))
}

export function formatCurrency(amount: number): string {
  return new Intl.NumberFormat('en-IN', {
    style: 'currency',
    currency: 'INR',
    maximumFractionDigits: 0,
  }).format(amount)
}

export function formatDate(dateStr: string): string {
  return new Intl.DateTimeFormat('en-IN', {
    day: '2-digit',
    month: 'short',
    year: 'numeric',
  }).format(new Date(dateStr))
}

export function formatDateTime(dateStr: string): string {
  return new Intl.DateTimeFormat('en-IN', {
    day: '2-digit',
    month: 'short',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
    hour12: true,
  }).format(new Date(dateStr))
}

export function formatRelative(dateStr: string): string {
  const diff = Date.now() - new Date(dateStr).getTime()
  const mins = Math.floor(diff / 60000)
  if (mins < 1) return 'Just now'
  if (mins < 60) return `${mins}m ago`
  const hrs = Math.floor(mins / 60)
  if (hrs < 24) return `${hrs}h ago`
  const days = Math.floor(hrs / 24)
  return `${days}d ago`
}

export const BOOKING_STATUS_CONFIG: Record<
  BookingStatus,
  { label: string; color: string; bg: string }
> = {
  PENDING: { label: 'Pending', color: 'text-amber-700', bg: 'bg-amber-50 border border-amber-200' },
  ASSIGNED: { label: 'Assigned', color: 'text-blue-700', bg: 'bg-blue-50 border border-blue-200' },
  ON_THE_WAY: { label: 'On The Way', color: 'text-violet-700', bg: 'bg-violet-50 border border-violet-200' },
  IN_PROGRESS: { label: 'In Progress', color: 'text-indigo-700', bg: 'bg-indigo-50 border border-indigo-200' },
  COMPLETED: { label: 'Completed', color: 'text-emerald-700', bg: 'bg-emerald-50 border border-emerald-200' },
  CANCELLED: { label: 'Cancelled', color: 'text-rose-700', bg: 'bg-rose-50 border border-rose-200' },
}

export const MECHANIC_STATUS_CONFIG: Record<
  MechanicStatus,
  { label: string; color: string; dot: string }
> = {
  AVAILABLE: { label: 'Available', color: 'text-emerald-700', dot: 'bg-emerald-500' },
  ON_DUTY: { label: 'On Duty', color: 'text-blue-700', dot: 'bg-blue-500' },
  BUSY: { label: 'Busy', color: 'text-amber-700', dot: 'bg-amber-500' },
  OFF_DUTY: { label: 'Off Duty', color: 'text-slate-500', dot: 'bg-slate-400' },
}

export const STATUS_CHART_COLORS: Record<BookingStatus, string> = {
  PENDING: '#F59E0B',
  ASSIGNED: '#3B82F6',
  ON_THE_WAY: '#8B5CF6',
  IN_PROGRESS: '#6366F1',
  COMPLETED: '#10B981',
  CANCELLED: '#F43F5E',
}
