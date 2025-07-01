package dminis.rpg.game.entity.battle;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Action {

    private ActionType type;
    private int weight;

     public enum ActionType {
        ATTACK, DEFENCE, ESCAPE, SKIP
    }
}
