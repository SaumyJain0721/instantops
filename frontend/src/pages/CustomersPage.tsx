import { useState, useEffect } from 'react'
import { Search, User, Phone, Mail } from '@/icons'
import { useCustomers } from '@/hooks/useCustomers'
import { TableSkeleton } from '@/components/ui/LoadingSpinner'
import { ErrorState, EmptyState } from '@/components/ui/States'
import { Pagination } from '@/components/ui/Pagination'

export function CustomersPage() {
  const [page, setPage] = useState(0)
  const [searchInput, setSearchInput] = useState('')
  const [search, setSearch] = useState('')
  const { data, loading, error, refresh } = useCustomers(page, 20, search)

  useEffect(() => {
    const t = setTimeout(() => { setSearch(searchInput); setPage(0) }, 350)
    return () => clearTimeout(t)
  }, [searchInput])

  if (error) return <ErrorState message={error} onRetry={refresh} />

  return (
    <div className="space-y-4">
      {/* Search */}
      <div className="rounded-xl border border-slate-200 bg-white p-4 shadow-sm">
        <div className="relative max-w-sm">
          <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-slate-400" />
          <input
            value={searchInput}
            onChange={e => setSearchInput(e.target.value)}
            placeholder="Search customers…"
            className="w-full rounded-lg border border-slate-200 bg-slate-50 py-2 pl-9 pr-3 text-sm placeholder:text-slate-400 focus:border-indigo-400 focus:outline-none focus:ring-1 focus:ring-indigo-100"
          />
        </div>
      </div>

      {loading ? (
        <TableSkeleton rows={12} cols={5} />
      ) : (
        <div className="overflow-hidden rounded-xl border border-slate-200 bg-white shadow-sm">
          <div className="overflow-x-auto">
            <table className="min-w-full text-sm">
              <thead>
                <tr className="border-b border-slate-100 bg-slate-50">
                  {['Customer', 'Email', 'Phone', 'Vehicles', 'Bookings'].map(h => (
                    <th key={h} className="px-4 py-3 text-left text-xs font-semibold uppercase tracking-wider text-slate-500">
                      {h}
                    </th>
                  ))}
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100">
                {data?.content.map(c => (
                  <tr key={c.id} className="hover:bg-slate-50 transition-colors">
                    <td className="px-4 py-3.5">
                      <div className="flex items-center gap-3">
                        <div className="flex h-9 w-9 items-center justify-center rounded-full bg-slate-100 text-slate-500 font-semibold text-sm">
                          {c.name.charAt(0).toUpperCase()}
                        </div>
                        <div>
                          <p className="font-medium text-slate-900">{c.name}</p>
                          <p className="text-xs text-slate-400">#{c.id}</p>
                        </div>
                      </div>
                    </td>
                    <td className="px-4 py-3.5">
                      <a href={`mailto:${c.email}`} className="flex items-center gap-1.5 text-slate-600 hover:text-indigo-600 transition-colors">
                        <Mail className="h-3.5 w-3.5" />
                        {c.email}
                      </a>
                    </td>
                    <td className="px-4 py-3.5">
                      <a href={`tel:${c.phone}`} className="flex items-center gap-1.5 text-slate-600 hover:text-indigo-600 transition-colors">
                        <Phone className="h-3.5 w-3.5" />
                        {c.phone}
                      </a>
                    </td>
                    <td className="px-4 py-3.5 text-slate-600">{c.vehicleCount}</td>
                    <td className="px-4 py-3.5 text-slate-600">{c.bookingCount}</td>
                  </tr>
                ))}
                {data?.content.length === 0 && (
                  <tr>
                    <td colSpan={5}>
                      <EmptyState
                        icon={<User className="h-5 w-5" />}
                        title="No customers found"
                        description="Try a different search term."
                      />
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>

          {data && data.totalPages > 1 && (
            <div className="border-t border-slate-100 px-4 py-3">
              <Pagination
                page={data.page}
                totalPages={data.totalPages}
                totalElements={data.totalElements}
                size={data.size}
                onPageChange={setPage}
              />
            </div>
          )}
        </div>
      )}
    </div>
  )
}
