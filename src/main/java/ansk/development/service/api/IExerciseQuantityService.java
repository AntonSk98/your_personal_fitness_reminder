package ansk.development.service.api;

import ansk.development.domain.ExerciseType;
import ansk.development.repository.api.IExerciseQuantityRepository;

/**
 * Service layer to access {@link IExerciseQuantityRepository}
 *
 * @author Anton Skripin
 */
public interface IExerciseQuantityService {

    long getExerciseNumberByType(ExerciseType type);
}
