import type { ApiResponse } from './types'

const BASE_URL = (import.meta.env.VITE_API_URL || '/api').replace(/\/$/, '')

class ApiError extends Error {
  constructor(
    public readonly status: number,
    message: string,
  ) {
    super(message)
    this.name = 'ApiError'
  }
}

async function request<T>(path: string, options?: RequestInit): Promise<T> {
  const url = `${BASE_URL}${path}`
  const res = await fetch(url, {
    headers: { 'Content-Type': 'application/json', ...options?.headers },
    ...options,
  })

  if (!res.ok) {
    let message = `HTTP ${res.status}: ${res.statusText}`
    try {
      const err = await res.json()
      if (err.message) message = err.message
    } catch {
      // ignore JSON parse error on error body
    }
    throw new ApiError(res.status, message)
  }

  const body: ApiResponse<T> = await res.json()
  return body.data
}

export async function get<T>(path: string): Promise<T> {
  return request<T>(path)
}

export async function patch<T>(path: string, body: unknown): Promise<T> {
  return request<T>(path, { method: 'PATCH', body: JSON.stringify(body) })
}
