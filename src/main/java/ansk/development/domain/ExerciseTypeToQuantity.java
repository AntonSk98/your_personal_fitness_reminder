package ansk.development.domain;

import ansk.development.configuration.ConfigRegistry;

import java.util.HashMap;
import java.util.Map;

/**
 * Domain class that maps type of exercise with its available quantity.
 * The available quantity number is configured via configuration file.
 *
 * @author Anton Skripin
 */
public final class ExerciseTypeToQuantity {

    private static final Map<ExerciseType, Integer> exerciseToQuantityMap = new HashMap<>();

    private ExerciseTypeToQuantity() {

    }

    public static long getNumberByType(ExerciseType type) {
        if (exerciseToQuantityMap.isEmpty()) {
            initialize();
        }
        return exerciseToQuantityMap.get(type);
    }

    private static void initialize() {
        exerciseToQuantityMap.put(ExerciseType.BICEPS, ConfigRegistry.props().exerciseSizes().getBiceps());
        exerciseToQuantityMap.put(ExerciseType.TRICEPS, ConfigRegistry.props().exerciseSizes().getTriceps());
        exerciseToQuantityMap.put(ExerciseType.CHEST, ConfigRegistry.props().exerciseSizes().getChest());
        exerciseToQuantityMap.put(ExerciseType.BACK, ConfigRegistry.props().exerciseSizes().getBack());
        exerciseToQuantityMap.put(ExerciseType.SHOULDERS, ConfigRegistry.props().exerciseSizes().getShoulders());
        exerciseToQuantityMap.put(ExerciseType.LEGS, ConfigRegistry.props().exerciseSizes().getLegs());
        exerciseToQuantityMap.put(ExerciseType.STRETCHING, ConfigRegistry.props().exerciseSizes().getStretching());
        exerciseToQuantityMap.put(ExerciseType.WEIGHT_FREE, ConfigRegistry.props().exerciseSizes().getWeightFree());
        exerciseToQuantityMap.put(ExerciseType.ABS, ConfigRegistry.props().exerciseSizes().getAbs());
        exerciseToQuantityMap.put(ExerciseType.PUSH_UPS, ConfigRegistry.props().exerciseSizes().getPushUps());
    }
}
