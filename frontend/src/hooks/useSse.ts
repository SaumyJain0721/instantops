import { useEffect, useRef, useState } from 'react'
import type { BookingStatusChangedEvent } from '@/api/types'

const MAX_EVENTS = 50
const API_BASE_URL = (import.meta.env.VITE_API_URL || '/api').replace(/\/$/, '')

export function useSse() {
  const [events, setEvents] = useState<BookingStatusChangedEvent[]>([])
  const [connected, setConnected] = useState(false)
  const esRef = useRef<EventSource | null>(null)

  useEffect(() => {
    const es = new EventSource(`${API_BASE_URL}/events`)
    esRef.current = es

    es.addEventListener('INIT', () => {
      setConnected(true)
    })

    es.addEventListener('BOOKING_STATUS_CHANGED', (e: MessageEvent) => {
      try {
        const event: BookingStatusChangedEvent = JSON.parse(e.data as string)
        setEvents(prev => [event, ...prev].slice(0, MAX_EVENTS))
      } catch {
        // ignore malformed events
      }
    })

    es.onerror = () => {
      setConnected(false)
      // Browser will auto-reconnect; we just mark disconnected
    }

    return () => {
      es.close()
      esRef.current = null
      setConnected(false)
    }
  }, [])

  return { events, connected }
}
