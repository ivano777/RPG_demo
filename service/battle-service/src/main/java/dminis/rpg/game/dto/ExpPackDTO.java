package dminis.rpg.game.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpPackDTO {
    Integer lvExp;
    Integer lckExp;
    Integer atkExp;
    Integer defExp;
    boolean taken;
}
