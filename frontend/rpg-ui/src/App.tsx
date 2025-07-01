import { Routes, Route } from 'react-router-dom'
import Home from './pages/Home'
import BattlePage from './pages/BattlePage'

export default function App() {
  return (
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/battle" element={<BattlePage />} />
      {/* Aggiungi qui altre rotte se servono in futuro */}
    </Routes>
  )
}
