package dminis.rpg.game.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BattleDTO {

    private Long id;
    private Long heroId;

    private CharacterSnapshotDTO heroSnapshot;
    private CharacterSnapshotDTO enemySnapshot;

    private String status;

    private Instant creationTime;
    private Instant lastUpdateTime;

    private boolean active;
    private int version;

    private List<TurnDTO> turns;
    private String startingPlayer;
}