package dminis.rpg.game.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TurnDTO {

    private Long id;

    private int index;

    private String actor;

    private int currentHeroHp;
    private int currentEnemyHp;

    private ActionDTO action;

    private Instant creationTime;

    private BattleDTO battle;
}