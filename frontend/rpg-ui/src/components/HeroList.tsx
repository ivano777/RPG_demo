import { useState } from 'react';
import styles from './HeroList.module.css';
import { Statuses, type HeroDTO } from '../types/hero';
import HeroInfoModal from './HeroInfoModal';
import { getHeroById, resumeStartBattle } from '../api/heroApi';
import { useNavigate } from 'react-router-dom';

interface Props {
  heroes: HeroDTO[];
}

export default function HeroList({ heroes }: Props) {
  const [selectedHero, setSelectedHero] = useState<HeroDTO | null>(null);
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleStartBattle = async (id: number) => {
  try {
    const data = await resumeStartBattle(id);
    navigate('/battle', { state: data });
  } catch (err) {
    console.error('Errore nel lancio della battaglia', err);
  }
};

  const handleShowDetails = async (id: number) => {
    setLoading(true);
    try {
      const data = await getHeroById(id);
      setSelectedHero(data);
    } catch (err) {
      console.error('Errore nel caricamento del dettaglio eroe', err);
    } finally {
      setLoading(false);
    }
  };

  if (heroes.length === 0) return <p>Nessun eroe creato.</p>;

  return (
    <div className={styles.container}>
      <ul className={styles.list}>
        {heroes.map((hero) => {
          const isDead = hero.status == Statuses.DEAD;
          return (
          <li key={hero.id} className={styles.item}>
            <button
              className={styles.infoButton}
              title="Mostra dettagli"
              onClick={() => handleShowDetails(hero.id)}
            >
              i
            </button>
            <span className={`${styles.name} ${isDead ? styles.deadName : ''}`}>
              {hero.name}
            </span>            <span className={styles.level}>Lv. {hero.level}</span>
            <button className={styles.startButton}
              title="Inizia battaglia"
              onClick={() => handleStartBattle(hero.id)}
              disabled={isDead}
            >
              ⚔
          </button>
          </li>
        )})}
      </ul>

      {loading && <p style={{ color: 'white', textAlign: 'center' }}>Caricamento...</p>}

      {selectedHero && (
        <HeroInfoModal
          hero={selectedHero}
          onClose={() => setSelectedHero(null)}
        />
      )}
    </div>
  );
}
