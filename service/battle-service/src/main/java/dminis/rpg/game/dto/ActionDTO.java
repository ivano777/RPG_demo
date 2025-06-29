package dminis.rpg.game.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActionDTO {

    private String type; // es. "ATTACK", "NONE"
    private int weight;

}