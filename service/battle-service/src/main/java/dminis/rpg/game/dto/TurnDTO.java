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
    private Long battleId;

    private int index;

    private String actor;

    private int heroHpAfter;
    private int enemyHpAfter;

    private ActionDTO action;

    private Instant creationTime;
}