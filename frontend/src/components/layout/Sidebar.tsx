import { NavLink } from '@/router'
import {
  LayoutDashboard,
  CalendarCheck2,
  Wrench,
  Users,
  Zap,
} from '@/icons'
import { cn } from '@/lib/utils'

const navItems = [
  { to: '/', label: 'Dashboard', icon: LayoutDashboard, end: true },
  { to: '/bookings', label: 'Bookings', icon: CalendarCheck2, end: false },
  { to: '/mechanics', label: 'Mechanics', icon: Wrench, end: false },
  { to: '/customers', label: 'Customers', icon: Users, end: false },
]

export function Sidebar() {
  return (
    <aside className="hidden h-screen w-60 shrink-0 flex-col bg-slate-900 text-slate-100 md:flex">
      {/* Brand */}
      <div className="flex items-center gap-2.5 px-5 py-5 border-b border-slate-800">
        <div className="flex h-8 w-8 items-center justify-center rounded-lg bg-blue-500">
          <Zap className="h-4.5 w-4.5 text-white" />
        </div>
        <div>
          <p className="text-sm font-semibold tracking-tight text-white">InstantOps</p>
          <p className="text-[10px] text-slate-400 uppercase tracking-widest">Operations</p>
        </div>
      </div>

      {/* Nav */}
      <nav className="flex-1 overflow-y-auto px-3 py-4 space-y-0.5">
        <p className="px-3 pb-2 text-[10px] font-semibold uppercase tracking-widest text-slate-500">
          Navigation
        </p>
        {navItems.map(({ to, label, icon: Icon, end }) => (
          <NavLink
            key={to}
            to={to}
            end={end}
            className={({ isActive }) =>
              cn(
                'flex items-center gap-3 rounded-lg px-3 py-2.5 text-sm font-medium transition-colors',
                isActive
                  ? 'bg-slate-700 text-white'
                  : 'text-slate-400 hover:bg-slate-800 hover:text-slate-100',
              )
            }
          >
            <Icon className="h-4 w-4 shrink-0" />
            {label}
          </NavLink>
        ))}
      </nav>

      {/* Footer */}
      <div className="border-t border-slate-800 px-5 py-4">
        <p className="text-[10px] text-slate-500">
          Vehicle Service Platform &copy; 2026
        </p>
      </div>
    </aside>
  )
}
