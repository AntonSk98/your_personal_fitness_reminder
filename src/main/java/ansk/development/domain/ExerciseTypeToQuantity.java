package ansk.development.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * Domain class that maps type of exercise with its available quantity.
 */
public final class ExerciseTypeToQuantity {

    private static final Map<ExerciseType, Long> exerciseToQuantityMap = new HashMap<>();

    static {
        exerciseToQuantityMap.put(ExerciseType.BICEPS, 7L);
        exerciseToQuantityMap.put(ExerciseType.TRICEPS, 5L);
        exerciseToQuantityMap.put(ExerciseType.CHEST, 5L);
        exerciseToQuantityMap.put(ExerciseType.BACK, 3L);
        exerciseToQuantityMap.put(ExerciseType.SHOULDERS, 4L);
        exerciseToQuantityMap.put(ExerciseType.LEGS, 4L);
        exerciseToQuantityMap.put(ExerciseType.WARM_UP_AND_STRETCHING, 22L);
    }

    private ExerciseTypeToQuantity() {

    }

    public static long getNumberByType(ExerciseType type) {
        return exerciseToQuantityMap.get(type);
    }
}
