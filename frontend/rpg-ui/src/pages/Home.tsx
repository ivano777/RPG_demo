import { useState, useEffect } from 'react'
import HeroForm from '../components/HeroForm'
import HeroList from '../components/HeroList'
import Message from '../components/Message'
import { createHero, getHeroes } from '../api/heroApi'
import type { HeroDTO } from '../types/hero'


export default function Home() {
  const [heroes, setHeroes] = useState<HeroDTO[]>([])
  const [success, setSuccess] = useState('')
  const [error, setError] = useState('')

  const loadHeroes = async () => {
    try {
      const list = await getHeroes()
      setHeroes(list)
    } catch {
      setError('Errore durante il caricamento eroi')
    }
  }

  useEffect(() => {
    loadHeroes()
  }, [])

  const handleCreate = async (name: string) => {
    try {
      const newHero = await createHero(name)
      setSuccess(`Eroe "${newHero.name}" creato con successo!`)
      setError('')
      await loadHeroes()
    } catch (err) {
      setError((err as Error).message)
      setSuccess('')
    }
  }

  return (
    <div>
      <h1>Benvenuto in ChronoField!</h1>
      <p>Inserisci il nome del tuo eroe:</p>

      <HeroForm
        onSuccess={handleCreate}
        onClearMessages={() => {
          setSuccess('')
          setError('')
        }}
      />

      {success && <Message text={success} type="success" />}
      {error && <Message text={error} type="error" />}

      <HeroList heroes={heroes} />
    </div>
  )
}
