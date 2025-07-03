package dminis.rpg.game.hero.dto;

import dminis.rpg.game.entity.hero.Hero;
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
    private int maxHp = 5;
    private int gold = 0;

    /* progressione globale */
    private int level = 1;     // max 12
    private int exp = 0;       // exp residua verso il prossimo livello

    private Hero.LifeStatus status = Hero.LifeStatus.ALIVE;   // ALIVE | DEAD

    /* statistiche con relativo livello/exp */
    private StatDto atk = new StatDto();
    private StatDto def = new StatDto();
    private StatDto lck = new StatDto();

    /* ------------------- sotto-dto ------------------- */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatDto {
        private int level = 1;   // cap = hero.level + 10
        private int exp = 0;     // exp residua verso il prossimo livello
    }
}
