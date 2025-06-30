import { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import BattleIntroModal from './BattleIntroModal';
import styles from './BattlePage.module.css';
import HealthBar from './HealthBar';
import { BattleStatuses, type BattleDTO } from '../types/battle';
import { ActionTypes, ActorTypes, type ActionType, type ActorType, type TurnDTO } from '../types/turn';
import { playTurn } from '../api/heroApi';

export default function BattlePage() {
  const { state } = useLocation();
  const battle = state as BattleDTO;
  const [showModal, setShowModal] = useState(true);
  // ----------------------- state locali -----------------------
  const { heroSnapshot:hero, enemySnapshot:enemy } = battle || {};
  const [turns,   setTurns]   = useState<TurnDTO[]>(battle.turns ?? []);
  const last      = turns.at(-1);               // undefined se lista vuota
  const [heroHp,  setHeroHp]  = useState<number>(last?.currentHeroHp  ?? battle.heroSnapshot.maxHp);
  const [enemyHp, setEnemyHp] = useState<number>(last?.currentEnemyHp ?? battle.enemySnapshot.maxHp);
  const [loading, setLoading] = useState(false);
  const [log, setLog] = useState<string[]>([]);


  useEffect(() => {
    if (!showModal && battle.startingPlayer === ActorTypes.ENEMY) {
      setTimeout(() => {
        handlePlayTurn(ActionTypes.SKIP, ActorTypes.ENEMY);
      }, 500);
    }
  }, [showModal]);

  useEffect(() => {
    if (turns.length === 0) return;

    const current = turns.at(-1)!;
    const actorName = current.actor === ActorTypes.HERO ? hero.name : enemy.name;
    const targetName = current.actor === ActorTypes.HERO ? enemy.name : hero.name;
    const damage = current.action.weight;

    let line = `${actorName} ha usato ${current.action.type.toLowerCase()}`;

    if (current.action.type === ActionTypes.ATTACK) {
      line += ` su ${targetName}, infliggendo ${damage} danni`;
    }

    setLog(prev => {
      const newLog = [...prev, line];
      return newLog.slice(-5); // tiene solo gli ultimi 5
    });
  }, [turns]);
  

  const handlePlayTurn = async (actionType: ActionType, actor: ActorType) => {
    setLoading(true);
    try {
      const turn = await playTurn(battle.id, actionType, actor);
      setHeroHp(turn.currentHeroHp);
      setEnemyHp(turn.currentEnemyHp);
      setTurns(prev => [...prev, turn]);

      const enemyAlive = turn.currentEnemyHp > 0;
      const heroAlive = turn.currentHeroHp > 0;

      // Se è stato l’eroe ad agire e il nemico è vivo, allora parte la reazione
      if (actor === ActorTypes.HERO && enemyAlive && heroAlive) {
        setTimeout(() => {
          handlePlayTurn(ActionTypes.SKIP, ActorTypes.ENEMY);
        }, 1500);
      }

    } catch (err) {
      console.error('Errore nel caricamento del turno', err);
    } finally {
      setLoading(false);
    }
  };


  return (
    <div className={styles.container}>
    {showModal && battle.status === BattleStatuses.TO_START &&(
        <BattleIntroModal
        hero={hero}
        enemy={enemy}
        starting={battle.startingPlayer}
        onClose={() => setShowModal(false)}
        />
    )}

    <div className={styles.enemyInfo}>
        <p><strong>{enemy.name}</strong></p>
        <p>Lv {enemy.level}</p>
        <div className={styles.barContainer}>
            <HealthBar current={enemyHp} max={enemy.maxHp} color="red" />
        </div>
    </div>

    <div className={styles.arena}>
      <div className={styles.battleLog}>
          {log.map((entry, i) => (
            <p key={i}>{entry}</p>
          ))}
      </div>
        {/* eventuale spazio vuoto, animazioni, barra vita, ecc */}
      {loading && <p style={{ color: 'white', textAlign: 'center' }}>Caricamento...</p>}
    </div>

    <div className={styles.footer}>
      <div className={styles.heroInfo}>
        <p><strong>{hero.name}</strong></p>
        <p>Lv {hero.level}</p>
        <div className={styles.barContainer}>
            <HealthBar current={heroHp} max={hero.maxHp} />
        </div>
    </div>
        <div className={styles.actionsGrid}>
        <button className={styles.button} onClick={() => handlePlayTurn(ActionTypes.ATTACK, ActorTypes.HERO)}>Attacca</button>
        <button className={styles.button} onClick={() => handlePlayTurn(ActionTypes.DEFEND, ActorTypes.HERO)}>Difendi</button>
        <button className={styles.button} onClick={() => handlePlayTurn(ActionTypes.ESCAPE, ActorTypes.HERO)}>Scappa</button>
        <button className={styles.button} onClick={() => handlePlayTurn(ActionTypes.SKIP, ActorTypes.HERO)}>Skippa</button>
        </div>
    </div>
    </div>
  );
}
