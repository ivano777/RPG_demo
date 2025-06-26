import styles from './HeroList.module.css'
import type { HeroDTO } from '../types/hero'

interface Props {
  heroes: HeroDTO[]
}

export default function HeroList({ heroes }: Props) {
  if (heroes.length === 0) return <p>Nessun eroe creato.</p>

  return (
    <div className={styles.container}>
      <ul className={styles.list}>
        {heroes.map((hero) => (
          <li key={hero.id} className={styles.item}>
            <span className={styles.name}>{hero.name}</span>
            <span className={styles.level}>Lv. {hero.level}</span>
          </li>
        ))}
      </ul>
    </div>
  )
}
