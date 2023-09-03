package ansk.development.service.methods;

import ansk.development.domain.ExerciseType;
import ansk.development.service.impl.ExerciseQuantityService;
import ansk.development.service.impl.FileManager;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.util.Random;

/**
 * Encapsulates the functionality to access random workout types.
 *
 * @author Anton Skripin
 */
public class ExerciseAccessManager {

    private static final Random RANDOM = new Random();

    private ExerciseAccessManager() {
    }

    public static InputFile getRandomExercise(ExerciseType type) {
        long filesInDirectory = ExerciseQuantityService.getService().getExerciseNumberByType(type);
        assert filesInDirectory > 0;
        long randomNumber = getRandomNumber(filesInDirectory);
        if (type == ExerciseType.BICEPS) {
            System.out.println(randomNumber);
        }
        return FileManager.fileManager().getGifFile(type.type(), String.valueOf(randomNumber));
    }

    private static long getRandomNumber(long filesInDirectory) {
        final long minimumNumber = 1;

        return RANDOM.nextLong(filesInDirectory - minimumNumber + 1) + minimumNumber;
    }
}
