package ansk.development.service.methods;

import ansk.development.domain.ExerciseType;
import ansk.development.domain.ExerciseTypeToQuantity;
import org.apache.commons.lang3.RandomUtils;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Encapsulates the functionality to access different exercise animations.
 *
 * @author Anton Skripin
 */
public class ExerciseAccessManager {

    private static final String EXERCISES_PATH_ENV_VARIABLE_NAME = "PATH_TO_EXERCISES";

    private static final String PATH_TO_EXERCISES = System.getenv(EXERCISES_PATH_ENV_VARIABLE_NAME);

    private ExerciseAccessManager() {

    }

    public static InputFile getRandomExercise(ExerciseType type) {
        long filesInDirectory = ExerciseTypeToQuantity.getNumberByType(type);
        assert filesInDirectory > 0;
        long randomNumber = RandomUtils.nextLong(1, filesInDirectory + 1);
        return ExerciseAccessManager.getGifFile(type.type(), String.valueOf(randomNumber));
    }

    private static InputFile getGifFile(String directory, String name) {
        return ExerciseAccessManager.getFile(directory, name, "gif");
    }

    private static InputFile getFile(String directory, String name, String extension) {
        final String path = String.format("%s/%s/%s.%s", PATH_TO_EXERCISES, directory, name, extension);
        try {
            InputStream file = new FileInputStream(path);
            return new InputFile(file, String.format("%s.%s", name, extension));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(String.format("No files are found in the following path: %s", path), e);
        }

    }
}
