import { useLocation } from '@/router'
import { RefreshCw, Wifi, WifiOff } from '@/icons'
import { cn } from '@/lib/utils'

interface HeaderProps {
  sseConnected: boolean
  onRefresh?: () => void
}

const pageTitles: Record<string, { title: string; subtitle: string }> = {
  '/': { title: 'Dashboard', subtitle: 'Real-time operations overview' },
  '/bookings': { title: 'Bookings', subtitle: 'Manage and track all service bookings' },
  '/mechanics': { title: 'Mechanics', subtitle: 'Field team status and performance' },
  '/customers': { title: 'Customers', subtitle: 'Customer profiles and vehicles' },
}

export function Header({ sseConnected, onRefresh }: HeaderProps) {
  const { pathname } = useLocation()
  const page = pageTitles[pathname] ?? { title: 'InstantOps', subtitle: '' }

  return (
    <header className="flex items-center justify-between border-b border-slate-200 bg-white px-4 py-3 sm:px-6 sm:py-4">
      <div>
        <h1 className="text-xl font-semibold text-slate-900">{page.title}</h1>
        <p className="text-sm text-slate-500">{page.subtitle}</p>
      </div>

      <div className="flex items-center gap-3">
        {/* Live indicator */}
        <div
          className={cn(
            'flex items-center gap-1.5 rounded-full border px-2.5 py-1 text-xs font-medium',
            sseConnected
              ? 'border-emerald-200 bg-emerald-50 text-emerald-700'
              : 'border-slate-200 bg-slate-50 text-slate-500',
          )}
        >
          {sseConnected ? (
            <>
              <span className="relative flex h-2 w-2">
                <span className="absolute inline-flex h-full w-full animate-ping rounded-full bg-emerald-400 opacity-75" />
                <span className="relative inline-flex h-2 w-2 rounded-full bg-emerald-500" />
              </span>
              <Wifi className="h-3.5 w-3.5" />
              Live
            </>
          ) : (
            <>
              <WifiOff className="h-3.5 w-3.5" />
              Offline
            </>
          )}
        </div>

        {/* Refresh */}
        {onRefresh && (
          <button
            onClick={onRefresh}
            className="flex items-center gap-1.5 rounded-lg border border-slate-200 bg-white px-3 py-1.5 text-sm text-slate-600 hover:bg-slate-50 transition-colors"
          >
            <RefreshCw className="h-3.5 w-3.5" />
            Refresh
          </button>
        )}
      </div>
    </header>
  )
}
