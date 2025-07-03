import { useState, useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import BattleIntroModal from '../components/BattleIntroModal';
import styles from './BattlePage.module.css';
import HealthBar from '../components/HealthBar';
import { BattleStatuses, type BattleDTO, type RewardDTO } from '../types/battle';
import { ActionTypes, ActorTypes, type ActionType, type ActorType, type TurnDTO } from '../types/turn';
import { applyReward, playTurn } from '../api/heroApi';
import EndBattleModal from '../components/EndBattleModal';

export default function BattlePage() {
  const navigate = useNavigate();
  const { state } = useLocation();
  const [battle, setBattle] = useState(state as BattleDTO);
  const [showModal, setShowModal] = useState(true);
  const [battleEnded, setBattleEnded] = useState(false);
  // ----------------------- state locali -----------------------
  const { heroSnapshot:hero, enemySnapshot:enemy } = battle || {};
  const [turns,   setTurns]   = useState<TurnDTO[]>(battle.turns ?? []);
  const last      = turns.at(-1);               // undefined se lista vuota
  const [heroHp,  setHeroHp]  = useState<number>(last?.currentHeroHp  ?? battle.heroSnapshot.maxHp);
  const [enemyHp, setEnemyHp] = useState<number>(last?.currentEnemyHp ?? battle.enemySnapshot.maxHp);
  const [loading, setLoading] = useState(false);
  const [log, setLog] = useState<{ text: string; actor: ActorType }[]>([]);
  const [reward, setReward] = useState<RewardDTO>();
  const ENEMY_WAITING_TIME = 0;

  useEffect(() => {
    if (!battle.active) {
    applyReward(battle.id)
      .then(res => {
        setReward(res); // salva la risposta della chiamata
        setBattleEnded(true); // mostra il modal solo dopo che hai il reward
      })
      .catch(err => {
        console.error('Errore nel recupero della ricompensa', err);
        setBattleEnded(true); // comunque chiudi la battaglia
      });
    }
  }, [battle.status]);


  useEffect(() => {
    if (!showModal && battle.startingPlayer === ActorTypes.ENEMY) {
      setTimeout(() => {
        handlePlayTurn(ActionTypes.SKIP, ActorTypes.ENEMY);
      }, ENEMY_WAITING_TIME);
    }
  }, [showModal]);

  useEffect(() => {
    if (turns.length === 0) return;

    const current = turns.at(-1)!;
    const actorName = current.actor === ActorTypes.HERO ? hero.name : enemy.name;
    const targetName = current.actor === ActorTypes.HERO ? enemy.name : hero.name;
    const actionWeight = current.action.weight;

    let line = `${actorName} ha usato ${current.action.type.toLowerCase()}`;

    if (current.action.type === ActionTypes.ATTACK) {
      line += ` su ${targetName}, infliggendo ${actionWeight} danni`;
    }

    if (current.action.type === ActionTypes.DEFENCE) {
      line += ` la sua difesa statica è aumentata di ${actionWeight}`;
    }

    if (current.action.type === ActionTypes.ESCAPE) {
      if(actionWeight <= 0){
        line += ` fuga fallita...`;
      }else{
        line += ` fuga riuscita!`;        
      }
    }

    setLog(prev => {
      const newLog = [...prev, { text: line, actor: current.actor }];
      return newLog.slice(-3);
    });
  }, [turns]);
  

  const handlePlayTurn = async (actionType: ActionType, actor: ActorType) => {
    setLoading(true);
    try {
      const turn = await playTurn(battle.id, actionType, actor);
      setBattle(turn.battle); 
      setHeroHp(turn.currentHeroHp);
      setEnemyHp(turn.currentEnemyHp);
      setTurns(prev => [...prev, turn]);

      const enemyAlive = turn.currentEnemyHp > 0;
      const heroAlive = turn.currentHeroHp > 0;

      // Se è stato l’eroe ad agire e il nemico è vivo, allora parte la reazione
      if (actor === ActorTypes.HERO && enemyAlive && heroAlive) {
        setTimeout(() => {
          handlePlayTurn(ActionTypes.SKIP, ActorTypes.ENEMY);
        }, ENEMY_WAITING_TIME);
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

    {battleEnded && (
      <EndBattleModal
        status={battle.status}
        heroName={hero.name}
        enemyName={enemy.name}
        reward={reward}
        onConfirm={() => navigate('/')}
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
            <p
              key={i}
              className={
                entry.actor === ActorTypes.HERO
                  ? styles.heroText   // gradiente verde
                  : styles.enemyText  // gradiente rosso
              }
            >
              {entry.text}
            </p>
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
        <button className={styles.button} onClick={() => handlePlayTurn(ActionTypes.DEFENCE, ActorTypes.HERO)}>Difendi</button>
        <button className={styles.button} onClick={() => handlePlayTurn(ActionTypes.ESCAPE, ActorTypes.HERO)}>Scappa</button>
        <button className={styles.button} onClick={() => handlePlayTurn(ActionTypes.SKIP, ActorTypes.HERO)}>Skippa</button>
        </div>
    </div>
    </div>
  );
}
