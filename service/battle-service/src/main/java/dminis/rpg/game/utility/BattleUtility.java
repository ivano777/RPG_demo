package dminis.rpg.game.utility;

import dminis.rpg.game.entity.CharacterSnapshot;

public class BattleUtility {

    public static CharacterSnapshot twist(CharacterSnapshot source){
        var target = new CharacterSnapshot();
        target.setLevel(randomInRange(0.3)*source.getLevel());
        target.setMaxHp(randomInRange(0.3)*source.getMaxHp());
        target.setAtk(randomInRange(0.5)*source.getAtk());
        target.setDef(randomInRange(0.5)*source.getDef());
        target.setLck(randomInRange(0.5)*source.getLck());
        return target;
    }

    /**
     *
     * @param delta
     * @return dato un input x (es. 0.3), generi un numero casuale nell’intervallo [1 - x, 1 + x].
     */
    public static int randomInRange(double delta) {
        return (int) Math.round(1.0 - delta + Math.random() * (2 * delta));
    }
}
