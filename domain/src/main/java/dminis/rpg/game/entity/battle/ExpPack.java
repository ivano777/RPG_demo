package dminis.rpg.game.entity.battle;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpPack{
    Integer lvExp;
    Integer lckExp;
    Integer atkExp;
    Integer defExp;
    boolean taken;
}