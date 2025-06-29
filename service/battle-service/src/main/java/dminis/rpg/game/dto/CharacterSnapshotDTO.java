package dminis.rpg.game.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CharacterSnapshotDTO {

    private String name;
    private int level;
    private int maxHp;
    private int atk;
    private int def;
    private int lck;
}