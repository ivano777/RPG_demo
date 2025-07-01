package dminis.rpg.game.entity.battle;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CharacterSnapshot {

    private String name;
    @Min(1) @Max(12)
    private int level;
    @Min(1)
    private int maxHp;
    @Min(1)
    @Embedded
    private StatSnapshot atk = new StatSnapshot();
    @Min(1)
    @Embedded
    private StatSnapshot def = new StatSnapshot();
    @Min(1)
    @Embedded
    private StatSnapshot lck = new StatSnapshot();
}
