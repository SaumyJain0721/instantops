import { get } from './client'
import type { DashboardData } from './types'
export async function fetchDashboard(): Promise<DashboardData> { return get<DashboardData>('/dashboard') }
