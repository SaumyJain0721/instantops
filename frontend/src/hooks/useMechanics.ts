import { useEffect, useState } from 'react'
import { fetchMechanics } from '@/api/mechanics'
import type { MechanicSummary } from '@/api/types'
export function useMechanics() {
 const [data,setData]=useState<MechanicSummary[]|null>(null); const [loading,setLoading]=useState(true); const [error,setError]=useState<string|null>(null)
 const load=async()=>{try{setLoading(true);setError(null);setData(await fetchMechanics())}catch(e){setError(e instanceof Error?e.message:'Failed to load mechanics')}finally{setLoading(false)}}
 useEffect(()=>{void load()},[])
 return {data,loading,error,refresh:load}
}
