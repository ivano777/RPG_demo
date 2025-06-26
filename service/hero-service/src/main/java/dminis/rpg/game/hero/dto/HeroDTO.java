package dminis.rpg.game.hero.dto;

import dminis.rpg.game.hero.entity.Hero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HeroDTO {

    private Long id;
    private String name;

    /* valori correnti di gioco */
    private int hp;
    private int gold;

    /* progressione globale */
    private int level;     // max 12
    private int exp;       // exp residua verso il prossimo livello

    private Hero.LifeStatus status;   // ALIVE | DEAD

    /* statistiche con relativo livello/exp */
    private StatDto atk;
    private StatDto def;
    private StatDto lck;

    /* ------------------- sotto-dto ------------------- */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatDto {
        private int level;   // cap = hero.level + 10
        private int exp;     // exp residua verso il prossimo livello
    }
}
