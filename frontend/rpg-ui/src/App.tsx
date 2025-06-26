import { Routes, Route } from 'react-router-dom'
import Home from './pages/Home'

export default function App() {
  return (
    <Routes>
      <Route path="/" element={<Home />} />
      {/* Aggiungi qui altre rotte se servono in futuro */}
    </Routes>
  )
}
