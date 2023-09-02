package ansk.development.repository;

import ansk.development.domain.ExerciseType;
import ansk.development.repository.api.IExerciseQuantityRepository;

import java.util.HashMap;
import java.util.Map;

public class ExerciseQuantityRepository implements IExerciseQuantityRepository {

    private static ExerciseQuantityRepository repository;
    private final Map<ExerciseType, Long> exerciseToQuantityMap;

    private ExerciseQuantityRepository() {
        exerciseToQuantityMap = new HashMap<>();
    }

    public static ExerciseQuantityRepository getRepository() {
        if (repository == null) {
            repository = new ExerciseQuantityRepository();
        }

        return repository;
    }

    @Override
    public void addExerciseTypeQuantity(ExerciseType type, long quantity) {
        this.exerciseToQuantityMap.put(type, quantity);
    }

    @Override
    public long getExerciseNumberByType(ExerciseType type) {
        return this.exerciseToQuantityMap.get(type);
    }
}
