import { useState } from 'react'
import styles from './HeroForm.module.css'
import Message from './Message'

interface HeroFormProps {
  onSuccess?: (name: string) => void
  onClearMessages?: () => void
}

export default function HeroForm({ onSuccess, onClearMessages }: HeroFormProps) {
  const [name, setName] = useState('')
  const [error, setError] = useState('')

  const alfanumerico = /^[a-zA-Z0-9]*$/

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value

    onClearMessages?.()

    if (value.length > 20) return

    if (alfanumerico.test(value)) {
      setName(value)
      setError('')
    } else {
      setError('Solo lettere e numeri')
    }
  }

  const handleSubmit = () => {
    if (!name) {
      setError('Il nome è richiesto')
      return
    }

    onSuccess?.(name)
    setName('')
  }

  const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Enter') {
      handleSubmit()
    }
  }

  return (
    <div className={styles.container}>
      <input
        type="text"
        value={name}
        onChange={handleChange}
        onKeyDown={handleKeyDown}
        placeholder="Nome eroe..."
        className={styles.input}
      />
      <button onClick={handleSubmit} className={styles.button}>Crea</button>

      {error && <Message text={error} type="error" />}
    </div>
  )
}
