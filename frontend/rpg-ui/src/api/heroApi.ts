import type { HeroDTO } from "../types/hero"

const BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080'

export async function createHero(name: string) {
  const res = await fetch(`${BASE_URL}/heroes/create`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ name }),
  })

  if (!res.ok) {
    let backendMsg = ''
    try {
      const data = await res.json()
      backendMsg = data?.message ?? JSON.stringify(data)
    } catch {
      backendMsg = await res.text()
    }
    throw new Error(backendMsg || `Errore ${res.status}`)
  }

  return await res.json()
}

export async function getHeroes(): Promise<HeroDTO[]> {
  const res = await fetch(`${BASE_URL}/heroes`)
  if (!res.ok) {
    throw new Error(`Errore ${res.status} nel recupero eroi`)
  }
  return await res.json()
}