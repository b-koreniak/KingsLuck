package koreniak.kingsluck.core;

import java.util.concurrent.ThreadLocalRandom;

public class RandomGenerator {
    public static Integer getRandom(Integer min, Integer max) {
        return ThreadLocalRandom.current().nextInt(min, max);
    }
}
