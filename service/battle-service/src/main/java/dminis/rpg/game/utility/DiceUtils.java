package dminis.rpg.game.utility;


import dminis.rpg.game.entity.battle.CharacterSnapshot;

import static dminis.rpg.game.utility.RNGProvider.nextDouble;
import static dminis.rpg.game.utility.RNGProvider.nextInt;

public class DiceUtils {

    public static int rollLck(CharacterSnapshot cs) {
        return rollStat(cs.getLevel(),
                cs.getLck().getLevel(),
                cs.getLck().getFlat());
    }

    public static int rollAtk(CharacterSnapshot cs) {
        return rollStat(cs.getLevel(),
                cs.getAtk().getLevel(),
                cs.getAtk().getFlat());
    }

    public static int rollReactDef(CharacterSnapshot cs) {
        double size = cs.getDef().getLevel() * 2/3d;
        double qty = cs.getLevel() / 2.0;

        return rollStat(size,
                qty,
                cs.getDef().getFlat());
    }

    public static int rollDef(CharacterSnapshot cs) {
        double size = cs.getDef().getLevel() / 2.0;
        double qty = cs.getLevel() / 2.0;

        return rollStat(size,
                qty,
                0);
    }

    private static int rollStat(double baseForDice,
                                double qty,
                                int flatBonus){
        return rollStat(baseForDice,qty,0,flatBonus);
    }

    private static int rollStat(double baseForDice,
                                double qty,
                                int steps,
                                int flatBonus) {
        DiceType dice = DiceType
                .fromNearestValue(baseForDice)
                .shift(steps);

        return dice.roll(qty) + flatBonus;
    }

    public enum DiceType {
        D0125(0.125), // 1/8
        D025(0.25), // 1/4
        D05(0.5),  // 1/2
        D1(1),
        D2(2),
        D4(4),
        D6(6),
        D8(8),
        D10(10),
        D12(12),
        D20(20);
        private final double sides;

        DiceType(double sides) {
            this.sides = sides;
        }

        private int roll(int qty){
            int total = 0;
            for (int i = 0; i < qty; i++) {
                total += roll();
            }
            return total;
        }

        public int roll(double qty){
            if(qty >= 1 ){
                return roll((int)Math.round(qty));
            } else if (qty > 0) {
                return chance(qty)*roll();
            } else if (qty == 0) {
                return 0;
            }

            throw new IllegalStateException(
                    "Invalid number of sides: " + sides);
        }

        public int roll() {
            if (sides > 1) {
                return nextInt((int)Math.round(sides)) + 1;
            } else if (sides == 1) {
                return 1;
            } else if (sides > 0) {
                return chance(sides);
            }
            throw new IllegalStateException(
                    "Invalid number of sides: " + sides);
        }

        public DiceType shift(int steps) {
            DiceType[] all = values();
            int newIdx = Math.min(Math.max(ordinal() + steps, 0), all.length - 1);
            return all[newIdx];
        }

        public DiceType oneSizeUp()   { return shift( 1); }
        public DiceType oneSizeDown() { return shift(-1); }

        public static int chance(double probability) {
            if (probability < 0.0 || probability > 1.0) {
                throw new IllegalArgumentException("probability must be in [0,1]");
            }
            return nextDouble() < probability ? 1 : 0;
        }

        public static DiceType fromNearestValue(double size) {
            DiceType best = null;
            double   bestDist = Double.MAX_VALUE;

            for (DiceType dt : values()) {
                double dist = Math.abs(dt.sides - size);

                if (dist < bestDist) {                    // distanza migliore
                    best = dt;
                    bestDist = dist;
                } else if (dist == bestDist               // stessa distanza
                        && dt.sides < best.sides) {    // ma sides più piccolo
                    best = dt;
                }
            }
            return best;   // non può mai essere null perché values() non è vuoto
        }
    }
}