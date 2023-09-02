package ansk.development.repository.api;

import ansk.development.domain.ExerciseType;

/**
 * Stores the number of exercises for each workout group.
 *
 * @author Anton Skripin
 */
public interface IExerciseQuantityRepository {

    void addExerciseTypeQuantity(ExerciseType type, long quantity);

    long getExerciseNumberByType(ExerciseType type);
}
