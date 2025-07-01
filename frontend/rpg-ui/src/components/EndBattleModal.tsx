import { BattleStatuses, type BattleStatus } from '../types/battle';
import styles from './EndBattleModal.module.css';

type Props = {
  status: BattleStatus;
  heroName: string;
  enemyName: string;
  onConfirm: () => void;    // es. navigate('/')
};

export default function EndBattleModal({
  status,
  heroName,
  enemyName,
  onConfirm,
}: Props) {
  const text =
    status === BattleStatuses.HERO_WIN
      ? `${heroName} ha vinto!`
      : `${enemyName} ha vinto!`;

  return (
    <div className={styles.overlay}>
      <div className={styles.modal}>
        <p className={styles.title}>{text}</p>
        <button className={styles.button} onClick={onConfirm}>
          Torna alla Home
        </button>
      </div>
    </div>
  );
}
