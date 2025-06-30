package dminis.rpg.game.entity.hero;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatProgress implements Progressable {

    @Min(1)
    private int level = 1;   // livello attuale della statistica

    @Min(0)
    private int exp = 0;     // xp accumulata verso il prossimo livello

    /**
     * Calcola l'xp richiesta per passare dal livello corrente al successivo:
     * 10 + (lvl^2)
     */
//    public int xpToNextLevel() {
//        return 10 + level * level;
//    }

    /**
     * Aggiunge xp e se necessario effettua uno o più level-up,
     * fermandosi se raggiunge il cap passato.
     *
     * @param delta       xp guadagnata
     * @param maxLevel    livello massimo consentito per questa statistica (hero lv +10)
     * @return true se è salito almeno di un livello
     */
//    public boolean gainExp(int delta, int maxLevel) {
//        boolean leveled = false;
//        exp += delta;
//
//        while (level < maxLevel && exp >= xpToNextLevel()) {
//            exp -= xpToNextLevel();
//            level++;
//            leveled = true;
//        }
//        // se era a livello cap e continua a prendere xp, semplicemente la scartiamo
//        if (level >= maxLevel) exp = 0;
//        return leveled;
//    }
}

