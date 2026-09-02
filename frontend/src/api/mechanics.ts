import { get } from './client'
import type { MechanicDetail, MechanicSummary } from './types'
export async function fetchMechanics(status?: string): Promise<MechanicSummary[]> { return get<MechanicSummary[]>(`/mechanics${status ? `?status=${encodeURIComponent(status)}` : ''}`) }
export async function fetchMechanicById(id:number): Promise<MechanicDetail> { return get<MechanicDetail>(`/mechanics/${id}`) }
