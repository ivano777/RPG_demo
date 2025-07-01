import type { HeroDTO } from '../types/hero';
import styles from './HeroInfoModal.module.css';

interface Props {
  hero: HeroDTO;
  onClose: () => void;
}

export default function HeroInfoModal({ hero, onClose }: Props) {
  return (
    <div className={styles.overlay}>
      <div className={styles.popup}>
        <h2 className={styles.title}>{hero.name}</h2>
          <div className={styles.statsGrid}>
            <span className={styles.label}>Livello:</span>
            <span className={styles.value}>{hero.level}</span>

            <span className={styles.label}>EXP:</span>
            <span className={styles.value}>{hero.exp}</span>

            <span className={styles.label}>Status:</span>
            <span className={styles.value}>{hero.status}</span>

            <span className={styles.label}>HP Max:</span>
            <span className={styles.value}>{hero.maxHp}</span>

            <div className={styles.statBlock}>
              <span className={styles.blockTitle}>ATK</span>
              <span className={styles.label}>Lv:</span>
              <span className={styles.value}>{hero.atk.level}</span>
              <span className={styles.label}>Exp:</span>
              <span className={styles.value}>{hero.atk.exp}</span>
            </div>

            <div className={styles.statBlock}>
              <span className={styles.blockTitle}>DEF</span>
              <span className={styles.label}>Lv:</span>
              <span className={styles.value}>{hero.def.level}</span>
              <span className={styles.label}>Exp:</span>
              <span className={styles.value}>{hero.def.exp}</span>
            </div>

            <div className={styles.statBlock}>
              <span className={styles.blockTitle}>LCK</span>
              <span className={styles.label}>Lv:</span>
              <span className={styles.value}>{hero.lck.level}</span>
              <span className={styles.label}>Exp:</span>
              <span className={styles.value}>{hero.lck.exp}</span>
            </div>

            <div className={styles.statBlock}>
              <span className={styles.blockTitle}>Gold</span>
              <span className={styles.blockTitle} style={{ fontSize: '1.5rem' }}>{hero.gold}</span>
            </div>
          </div>

        <button onClick={onClose} className={styles.closeButton}>Chiudi</button>
      </div>
    </div>
  );
}
