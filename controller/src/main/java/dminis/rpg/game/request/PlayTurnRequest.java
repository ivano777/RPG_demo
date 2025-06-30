package dminis.rpg.game.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayTurnRequest {
    private String actionType;
    private String actor;
}
