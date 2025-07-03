import type { CharacterSnapshot } from '../types/characterSnapshot'
import { ActorTypes } from '../types/turn';

interface Props {
  hero: CharacterSnapshot;
  enemy: CharacterSnapshot;
  starting: string
  onClose: () => void;
}

export default function BattleIntroModal({ hero, enemy, starting, onClose }: Props) {
  return (
    <div style={{
      position: 'fixed',
      top: 0, left: 0, right: 0, bottom: 0,
      backgroundColor: 'rgba(0,0,0,0.7)',
      display: 'flex',
      justifyContent: 'center',
      alignItems: 'center',
      zIndex: 999
    }}>
      <div style={{
        backgroundColor: '#222',
        color: 'white',
        padding: '2rem',
        borderRadius: '8px',
        maxWidth: '400px',
        textAlign: 'center'
      }}>
        <h2>Scontro!</h2>
        <p><strong>{hero.name} Lv. {hero.level}</strong> ha incontrato <strong>{enemy.name} Lv. {enemy.level}</strong>!</p>
        <p><strong>{(starting === ActorTypes.HERO ? hero.name : enemy.name)} è stato più veloce, sarà il primo ad attaccare</strong>!</p>

        <p>Preparati alla battaglia...</p>
        <button style={{ marginTop: '1rem' }} onClick={onClose}>Inizia</button>
      </div>
    </div>
  );
}
