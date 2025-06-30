package dminis.rpg.game.utility;


import dminis.rpg.game.entity.battle.CharacterSnapshot;

import java.security.SecureRandom;

public class DiceUtils {
    static final SecureRandom RNG = new SecureRandom();

    public static int rollLck(CharacterSnapshot cs) {
        return rollStat(cs.getLevel(), cs.getLck());
    }
    public static int rollAtk(CharacterSnapshot cs) {
        return rollStat(cs.getLevel(), cs.getAtk());
    }
    public static int rollReactDef(CharacterSnapshot cs) {
        return rollStat((int)Math.round(cs.getLevel()/2.0), cs.getLck() -1);
    }
    public static int rollDef(CharacterSnapshot cs) {
        return rollStat((int)Math.round(cs.getLevel()/4.0), cs.getLck() -2);
    }


    private static int rollStat(int level, int stat) {
        return DiceType.fromNearestValue(stat).roll(level);
    }

    public enum DiceType {
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

        public int roll (int qty){
            int total = 0;
            for (int i = 0; i < qty; i++) {
                total += roll();
            }
            return total;
        }

        public int roll() {
            if (sides > 1) {
                return RNG.nextInt((int)Math.round(sides)) + 1;
            } else if (sides == 1) {
                return 1;
            } else if (sides > 0) {
                return chance(sides);
            }
            throw new IllegalStateException(
                    "Invalid number of sides: " + sides);
        }
        public static int chance(double probability) {
            if (probability < 0.0 || probability > 1.0) {
                throw new IllegalArgumentException("probability must be in [0,1]");
            }
            return RNG.nextDouble() < probability ? 1 : 0;
        }

        public static DiceType fromNearestValue(double value) {
            DiceType best = null;
            double   bestDist = Double.MAX_VALUE;

            for (DiceType dt : values()) {
                double dist = Math.abs(dt.sides - value);

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