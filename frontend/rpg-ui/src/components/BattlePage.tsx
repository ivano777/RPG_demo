import { useState } from 'react';
import { useLocation } from 'react-router-dom';
import BattleIntroModal from './BattleIntroModal';
import styles from './BattlePage.module.css';
import HealthBar from './HealthBar';

export default function BattlePage() {
  const { state } = useLocation();
    //  const { hero, enemy } = state || {};
  const [showModal, setShowModal] = useState(true);

  const hero = {
    name: 'TestHero',
    level: 5,
    atk: 10,
    def: 5,
    spd: 7,
    maxHp: 50,
    currentHp: 27,
    lv: 5,
  };

  const enemy = {
    name: 'StubGoblin',
    level: 5,
    atk: 8,
    def: 4,
    spd: 6,
    currentHp: 16,
    maxHp: 40,
    lv: 5,
  };

  return (
    <div className={styles.container}>
    {showModal && (
        <BattleIntroModal
        hero={hero}
        enemy={enemy}
        onClose={() => setShowModal(false)}
        />
    )}

    <div className={styles.enemyInfo}>
        <p><strong>{enemy.name}</strong></p>
        <p>Lv {enemy.level}</p>
        <div className={styles.barContainer}>
            <HealthBar current={enemy.currentHp} max={enemy.maxHp} color="red" />
        </div>
    </div>

    <div className={styles.arena}>
        {/* eventuale spazio vuoto, animazioni, barra vita, ecc */}
    </div>

    <div className={styles.footer}>
        <div className={styles.heroInfo}>
        <p><strong>{hero.name}</strong></p>
        <p>Lv {hero.level}</p>
        <div className={styles.barContainer}>
            <HealthBar current={hero.currentHp} max={hero.maxHp} />
        </div>
    </div>

        <div className={styles.actionsGrid}>
        <button className={styles.button}>Attacca</button>
        <button className={styles.button}>Difendi</button>
        <button className={styles.button}>Scappa</button>
        <button className={styles.button}>Skippa</button>
        </div>
    </div>
    </div>
  );
}
