import { useState, useEffect } from 'react';
import styles from './HeroList.module.css';
import { Statuses, type HeroDTO } from '../types/hero';
import HeroInfoModal from './HeroInfoModal';
import { deleteHero, getHeroById, getHeroes, resumeStartBattle } from '../api/heroApi';
import { useLocation, useNavigate } from 'react-router-dom';

interface Props {
  heroes: HeroDTO[];
}

export default function HeroList({ heroes }: Props) {
  const [confirmDeleteId, setConfirmDeleteId] = useState<number | null>(null);
  const [confirmDeleteName, setConfirmDeleteName] = useState<string | null>(null);
  const [selectedHero, setSelectedHero] = useState<HeroDTO | null>(null);
  const [loading, setLoading] = useState(false);
  const [heroList, setHeroList] = useState<HeroDTO[]>(heroes);

  const navigate = useNavigate();
  const location = useLocation();

  useEffect(() => {
    setHeroList(heroes);
  }, [heroes]);

  useEffect(() => {
    const fetchHeroes = async () => {
      const data = await getHeroes();
      setHeroList(data);
    };

    fetchHeroes();
  }, [location]);
    
  const handleDeleteHero = async (id: number) => {
    try {
      await deleteHero(id);
      setConfirmDeleteId(null);
      setConfirmDeleteName(null);
      const updated = await getHeroes();
      setHeroList(updated);
    } catch (err) {
      console.error('Errore nella cancellazione dell\'eroe', err);
    }
  };

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
        {[...heroList].reverse().map((hero) => {
          const isDead = hero.status == Statuses.DEAD;
          return (
          <li key={hero.id} className={styles.item}>
              <button className={styles.deleteButton}
                title="Elimina eroe"
                onClick={() => {
                  setConfirmDeleteId(hero.id);
                  setConfirmDeleteName(hero.name);
                }}
              >
              -
            </button>
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
      {confirmDeleteId !== null && (
        <div className={styles.modalOverlay}>
          <div className={styles.modal}>
            <p>Sei sicuro di voler eliminare <strong>{confirmDeleteName}</strong>?</p>
            <div className={styles.modalActions}>
              <button onClick={() => handleDeleteHero(confirmDeleteId)}>Sì</button>
              <button onClick={() => setConfirmDeleteId(null)}>No</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
