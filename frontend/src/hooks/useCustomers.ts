import { useEffect, useState } from 'react'
import { fetchCustomers } from '@/api/customers'
import type { CustomerSummary, PageResponse } from '@/api/types'

export function useCustomers(page = 0, size = 20, search = '') {
  const [data, setData] = useState<PageResponse<CustomerSummary> | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const load = async () => {
    try {
      setLoading(true)
      setError(null)
      const d = await fetchCustomers(page, size, search)
      setData(d)
    } catch (e) {
      setError(e instanceof Error ? e.message : 'Failed to load customers')
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => { void load() }, [page, size, search]) // eslint-disable-line react-hooks/exhaustive-deps

  return { data, loading, error, refresh: load }
}
