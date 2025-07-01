import type { BattleDTO, RewardDTO } from "../types/battle"
import type { HeroDTO } from "../types/hero"
import type { ActionType, ActorType, TurnDTO } from "../types/turn"

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

export async function getHeroById(id: number): Promise<HeroDTO> {
  const res = await fetch(`${BASE_URL}/heroes/${id}`)
  if (!res.ok) {
    throw new Error(`Errore ${res.status} nel recupero eroi`)
  }
  return await res.json()
}

export async function resumeStartBattle(id: number): Promise<BattleDTO> {
  const res = await fetch(`${BASE_URL}/battle/resume-start`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ id }),
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

export async function playTurn(battleId: number, actionType: ActionType, actor: ActorType ): Promise<TurnDTO> {
  const res = await fetch(`${BASE_URL}/battle/${battleId}/turn`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ actionType, actor }),
  });
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

export async function applyReward(battleId: number): Promise<RewardDTO> {
  const res = await fetch(`${BASE_URL}/battle/${battleId}/reward`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' }});
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


export async function deleteHero(id: number){
    await fetch(`${BASE_URL}/heroes/${id}`, {
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' }});
}