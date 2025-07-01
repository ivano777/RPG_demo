package dminis.rpg.game.entity.battle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ExpPack{
    Integer lvExp;
    Integer lckExp;
    Integer atkExp;
    Integer defExp;
}