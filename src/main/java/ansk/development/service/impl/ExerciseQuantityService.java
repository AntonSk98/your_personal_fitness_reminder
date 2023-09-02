package ansk.development.service.impl;

import ansk.development.domain.ExerciseType;
import ansk.development.repository.ExerciseQuantityRepository;
import ansk.development.service.api.IExerciseQuantityService;

/**
 * Implementation of {@link IExerciseQuantityService}.
 *
 * @author Anton Skripin
 */
public class ExerciseQuantityService implements IExerciseQuantityService {

    private static ExerciseQuantityService exerciseQuantityService;

    public static ExerciseQuantityService getService() {
        if (exerciseQuantityService == null) {
            exerciseQuantityService = new ExerciseQuantityService();
            initialize();
        }
        return exerciseQuantityService;
    }

    private static void initialize() {
        for (ExerciseType exerciseType : ExerciseType.values()) {
            long exerciseCount = FileManager.fileManager().getFileCount(exerciseType.type());
            ExerciseQuantityRepository.getRepository().addExerciseTypeQuantity(exerciseType, exerciseCount);
        }
    }

    @Override
    public long getExerciseNumberByType(ExerciseType type) {
        return ExerciseQuantityRepository.getRepository().getExerciseNumberByType(type);
    }
}
