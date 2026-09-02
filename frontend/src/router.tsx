import { createContext, useContext, useEffect, useMemo, useState, type ReactNode } from 'react'

interface RouterContextValue { pathname: string; navigate: (to: string) => void }
const RouterContext = createContext<RouterContextValue | null>(null)

export function RouterProvider({ children }: { children: ReactNode }) {
  const [pathname, setPathname] = useState(() => window.location.pathname || '/')
  useEffect(() => {
    const onPop = () => setPathname(window.location.pathname || '/')
    window.addEventListener('popstate', onPop)
    return () => window.removeEventListener('popstate', onPop)
  }, [])
  const value = useMemo(() => ({
    pathname,
    navigate: (to: string) => { window.history.pushState({}, '', to); setPathname(to) },
  }), [pathname])
  return <RouterContext.Provider value={value}>{children}</RouterContext.Provider>
}

export function useRouter() {
  const ctx = useContext(RouterContext)
  if (!ctx) throw new Error('useRouter must be used inside RouterProvider')
  return ctx
}

export function NavLink({ to, children, className, end }: { to: string; children: ReactNode; className?: string | ((args: { isActive: boolean }) => string); end?: boolean }) {
  const { pathname, navigate } = useRouter()
  const isActive = end ? pathname === to : (to === '/' ? pathname === '/' : pathname.startsWith(to))
  const classes = typeof className === 'function' ? className({ isActive }) : className
  return <a href={to} className={classes} onClick={(e) => { e.preventDefault(); navigate(to) }}>{children}</a>
}

export function useLocation() { return useRouter() }
