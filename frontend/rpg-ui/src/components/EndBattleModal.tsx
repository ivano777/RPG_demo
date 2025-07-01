import { BattleStatuses, type BattleStatus, type RewardDTO } from '../types/battle';
import styles from './EndBattleModal.module.css';

type Props = {
  status: BattleStatus;
  heroName: string;
  enemyName: string;
  reward: RewardDTO | undefined;
  onConfirm: () => void;
};

export default function EndBattleModal({
  status,
  heroName,
  enemyName,
  reward,
  onConfirm,
}: Props) {

  return (
    <div className={styles.overlay}>
      <div className={styles.modal}>
        {status === BattleStatuses.HERO_WIN && (
          <>
            <p className={styles.title}>{heroName} ha vinto!</p>
            <div className={styles.rewardSection}>
              <p className={styles.lvExpLine}>
                Hai guadagnato <strong>{reward?.expPack.lvExp} XP</strong> <span className={styles.lvLabel}></span>
              </p>

              <div className={styles.secondaryExp}>
                <p><strong>{reward?.expPack.atkExp}</strong> Atk xp</p>
                <p><strong>{reward?.expPack.defExp}</strong> Def xp</p>
                <p><strong>{reward?.expPack.lckExp}</strong> Lck xp</p>
              </div>

              <p className={styles.subtitle}>Nuovo stato del personaggio:</p>
                <div className={styles.heroStatsGrid}>
                  <span className={styles.label}>Livello:</span>
                  <span className={styles.value}>{reward?.hero.level}</span>

                  <span className={styles.label}>HP Max:</span>
                  <span className={styles.value}>{reward?.hero.maxHp}</span>

                  <span className={styles.label}>ATK:</span>
                  <span className={styles.value}>Lv {reward?.hero.atk}</span>

                  <span className={styles.label}>DEF:</span>
                  <span className={styles.value}>Lv {reward?.hero.def}</span>

                  <span className={styles.label}>LCK:</span>
                  <span className={styles.value}>Lv {reward?.hero.lck}</span>
                </div>
            </div>
          </>
        )}
        {status === BattleStatuses.ENEMY_WIN && (
          <>
            <p className={styles.loserTitle}>Hai perso!</p>
            <p className={styles.loserSubtitle}>Sei stato sconfitto da {enemyName}</p>
          </>
        )}
        {status === BattleStatuses.ESCAPED && (
          <>
            <p className={styles.title}>Fuga riuscita!</p>
            <p className={styles.loserSubtitle}>Battaglia contro {enemyName} conclusa disonorevolmente...</p>
          </>
        )}
        <button className={styles.button} onClick={onConfirm}>
          Torna alla Home
        </button>
      </div>
    </div>
  );
}
