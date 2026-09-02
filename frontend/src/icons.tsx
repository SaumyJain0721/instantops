import type { SVGProps, ReactNode } from 'react'

type Props = SVGProps<SVGSVGElement>
function Icon({ children, ...props }: Props & { children?: ReactNode }) {
  return <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round" aria-hidden="true" {...props}>{children}</svg>
}
const p = (d: string) => <path d={d} />
export const Search = (props: Props) => <Icon {...props}>{p('m21 21-4.3-4.3M10.8 18a7.2 7.2 0 1 1 0-14.4 7.2 7.2 0 0 1 0 14.4Z')}</Icon>
export const SlidersHorizontal = (props: Props) => <Icon {...props}>{p('M3 6h7M14 6h7M3 12h11M18 12h3M3 18h3M10 18h11M10 4v4M14 10v4M6 16v4')}</Icon>
export const ChevronUp = (props: Props) => <Icon {...props}>{p('m6 15 6-6 6 6')}</Icon>
export const ChevronDown = (props: Props) => <Icon {...props}>{p('m6 9 6 6 6-6')}</Icon>
export const ChevronLeft = (props: Props) => <Icon {...props}>{p('m15 18-6-6 6-6')}</Icon>
export const ChevronRight = (props: Props) => <Icon {...props}>{p('m9 18 6-6-6-6')}</Icon>
export const X = (props: Props) => <Icon {...props}>{p('M6 6l12 12M18 6 6 18')}</Icon>
export const Wrench = (props: Props) => <Icon {...props}>{p('m14.7 6.3 3-3a5 5 0 0 0 0 6.4l-7.8 7.8a2.2 2.2 0 1 0 3.1 3.1l7.8-7.8a5 5 0 0 0 6.4 0l-3 3')}</Icon>
export const Car = (props: Props) => <Icon {...props}>{p('M5 17h14l1-5-2-5H6l-2 5 1 5Zm0 0v2m14-2v2M7 17h.01M17 17h.01M4 12h16')}</Icon>
export const User = (props: Props) => <Icon {...props}>{p('M20 21a8 8 0 0 0-16 0M12 13a4 4 0 1 0 0-8 4 4 0 0 0 0 8Z')}</Icon>
export const Phone = (props: Props) => <Icon {...props}>{p('M5 4h3l2 5-2 1.5a15 15 0 0 0 5.5 5.5L15 14l5 2v3c0 1-1 1-2 1C10 20 4 14 4 6c0-1 0-2 1-2Z')}</Icon>
export const Mail = (props: Props) => <Icon {...props}>{p('M4 6h16v12H4zM4 7l8 6 8-6')}</Icon>
export const Calendar = (props: Props) => <Icon {...props}>{p('M6 3v3M18 3v3M4 8h16M5 5h14a1 1 0 0 1 1 1v13H4V6a1 1 0 0 1 1-1Z')}</Icon>
export const CalendarCheck2 = (props: Props) => <Icon {...props}>{p('M6 3v3M18 3v3M4 8h16M5 5h14a1 1 0 0 1 1 1v14H4V6a1 1 0 0 1 1-1ZM8 14l2 2 5-5')}</Icon>
export const DollarSign = (props: Props) => <Icon {...props}>{p('M12 3v18M16 7.5c-.8-1-2-1.5-4-1.5-2.2 0-4 1.1-4 3s1.5 2.8 4 3c2.5.2 4 1.1 4 3.2 0 1.9-1.7 3.3-4 3.3-2 0-3.5-.7-4.3-1.8')}</Icon>
export const CheckCircle2 = (props: Props) => <Icon {...props}>{p('M21 12a9 9 0 1 1-18 0 9 9 0 0 1 18 0Zm-9-4v4l2.5 2.5')}</Icon>
export const CheckCircle = (props: Props) => <Icon {...props}>{p('M21 12a9 9 0 1 1-18 0 9 9 0 0 1 18 0Zm-13 0 2.5 2.5L16 9')}</Icon>
export const XCircle = (props: Props) => <Icon {...props}>{p('M21 12a9 9 0 1 1-18 0 9 9 0 0 1 18 0ZM9 9l6 6M15 9l-6 6')}</Icon>
export const Clock = (props: Props) => <Icon {...props}>{p('M12 6v6l4 2M21 12a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z')}</Icon>
export const TrendingUp = (props: Props) => <Icon {...props}>{p('M3 17l6-6 4 4 7-8M15 7h5v5')}</Icon>
export const Users = (props: Props) => <Icon {...props}>{p('M16 21v-2a4 4 0 0 0-4-4H7a4 4 0 0 0-4 4v2M9.5 11a4 4 0 1 0 0-8 4 4 0 0 0 0 8ZM17 8a3 3 0 0 1 0 6M21 21v-2a4 4 0 0 0-3-3')}</Icon>
export const Zap = (props: Props) => <Icon {...props}>{p('m13 2-9 12h7l-1 8 9-12h-7l1-8Z')}</Icon>
export const Wifi = (props: Props) => <Icon {...props}>{p('M5 9a11 11 0 0 1 14 0M8 12a6 6 0 0 1 8 0M11 15a2 2 0 0 1 2 0M12 19h.01')}</Icon>
export const WifiOff = (props: Props) => <Icon {...props}>{p('M3 3l18 18M5 9a11 11 0 0 1 6-2M13 7a11 11 0 0 1 6 2M8 12a6 6 0 0 1 3-1M13 11a6 6 0 0 1 3 1M11 15a2 2 0 0 1 1-.3M12 19h.01')}</Icon>
export const RefreshCw = (props: Props) => <Icon {...props}>{p('M20 11a8 8 0 0 0-14.7-4L3 10m0-5v5h5M4 13a8 8 0 0 0 14.7 4L21 14m0 5v-5h-5')}</Icon>
export const LayoutDashboard = (props: Props) => <Icon {...props}>{p('M4 4h6v7H4zM14 4h6v4h-6zM14 12h6v8h-6zM4 15h6v5H4z')}</Icon>
export const Star = (props: Props) => <Icon {...props} fill="currentColor">{p('m12 3 2.8 5.7 6.2.9-4.5 4.4 1.1 6.2-5.6-3-5.6 3 1.1-6.2L3 9.6l6.2-.9L12 3Z')}</Icon>
export const Loader2 = (props: Props) => <Icon {...props}>{p('M12 2v4M12 18v4M4.9 4.9l2.8 2.8M16.3 16.3l2.8 2.8M2 12h4M18 12h4M4.9 19.1l2.8-2.8M16.3 7.7l2.8-2.8')}</Icon>
export const AlertCircle = (props: Props) => <Icon {...props}>{p('M12 8v4M12 16h.01M21 12a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z')}</Icon>
export const FileText = (props: Props) => <Icon {...props}>{p('M6 3h8l4 4v14H6zM14 3v5h5M9 12h6M9 16h6')}</Icon>

export const Activity = (props: Props) => <Icon {...props}>{p('M3 12h3l2-7 4 14 2-7h7')}</Icon>
