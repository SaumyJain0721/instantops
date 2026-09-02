import { get } from './client'
import type { ServiceSummary } from './types'

export async function fetchServices(): Promise<ServiceSummary[]> {
  return get<ServiceSummary[]>('/services')
}
