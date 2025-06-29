package dminis.rpg.game.entity;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CharacterSnapshot {

    private String name;
    @Min(1) @Max(12)
    private int level;
    @Min(1)
    private int maxHp;
    @Min(1)
    private int atk;
    @Min(1)
    private int def;
    @Min(1)
    private int lck;
}
