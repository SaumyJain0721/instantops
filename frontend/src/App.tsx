import { RouterProvider, useRouter } from '@/router'
import { Sidebar } from '@/components/layout/Sidebar'
import { Header } from '@/components/layout/Header'
import { useSse } from '@/hooks/useSse'
import { DashboardPage } from '@/pages/DashboardPage'
import { BookingsPage } from '@/pages/BookingsPage'
import { MechanicsPage } from '@/pages/MechanicsPage'
import { CustomersPage } from '@/pages/CustomersPage'
import { cn } from '@/lib/utils'

function AppContent() {
  const { pathname } = useRouter()
  const { connected, events } = useSse()
  const liveEventKey = events[0]?.timestamp
  const page = pathname.startsWith('/bookings') ? <BookingsPage liveEventKey={liveEventKey} />
    : pathname.startsWith('/mechanics') ? <MechanicsPage liveEventKey={liveEventKey} />
    : pathname.startsWith('/customers') ? <CustomersPage />
    : <DashboardPage liveEventKey={events[0]?.timestamp} />
  return (
    <div className="flex min-h-screen bg-slate-50 text-slate-900">
      <Sidebar />
      <div className="flex min-w-0 flex-1 flex-col">
        <nav className="flex items-center gap-1 overflow-x-auto border-b border-slate-200 bg-white px-3 py-2 md:hidden">
          {[
            ['/', 'Dashboard'],
            ['/bookings', 'Bookings'],
            ['/mechanics', 'Mechanics'],
            ['/customers', 'Customers'],
          ].map(([to, label]) => (
            <a
              key={to}
              href={to}
              onClick={(e) => {
                e.preventDefault()
                window.history.pushState({}, '', to)
                window.dispatchEvent(new PopStateEvent('popstate'))
              }}
              className={cn(
                'whitespace-nowrap rounded-lg px-3 py-2 text-xs font-medium',
                pathname === to || (to !== '/' && pathname.startsWith(to))
                  ? 'bg-slate-900 text-white'
                  : 'text-slate-500 hover:bg-slate-100',
              )}
            >
              {label}
            </a>
          ))}
        </nav>
        <Header sseConnected={connected} />
        <main className="flex-1 overflow-y-auto p-4 sm:p-6">{page}</main>
      </div>
    </div>
  )
}

export default function App() {
  return <RouterProvider><AppContent /></RouterProvider>
}
