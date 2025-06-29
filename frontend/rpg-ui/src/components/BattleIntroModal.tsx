// components/BattleIntroModal.tsx
interface Props {
  hero: any; // Puoi usare un tipo più preciso se ce l'hai
  enemy: any;
  onClose: () => void;
}

export default function BattleIntroModal({ hero, enemy, onClose }: Props) {
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
        <p><strong>{hero.name}</strong> ha incontrato <strong>{enemy.name}</strong>!</p>
        <p>Preparati alla battaglia...</p>
        <button style={{ marginTop: '1rem' }} onClick={onClose}>Inizia</button>
      </div>
    </div>
  );
}
