package dminis.rpg.game.utility;

import lombok.Getter;

import java.security.SecureRandom;
import java.util.random.RandomGenerator;


public class RNGProvider {

    @Getter
    private static final RandomGenerator RNG = new SecureRandom();

    public static int nextInt(int bound) {
        return RNG.nextInt(bound);
    }

    public static double nextDouble() {
        return RNG.nextDouble();
    }
}
