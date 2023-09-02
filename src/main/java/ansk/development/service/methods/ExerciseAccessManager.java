package ansk.development.service.methods;

import ansk.development.domain.ExerciseType;
import ansk.development.service.impl.ExerciseQuantityService;
import ansk.development.service.impl.FileManager;
import org.apache.commons.lang3.RandomUtils;
import org.telegram.telegrambots.meta.api.objects.InputFile;

/**
 * Encapsulates the functionality to access random workout types.
 *
 * @author Anton Skripin
 */
public class ExerciseAccessManager {

    private ExerciseAccessManager() {
    }

    public static InputFile getRandomExercise(ExerciseType type) {
        long filesInDirectory = ExerciseQuantityService.getService().getExerciseNumberByType(type);
        assert filesInDirectory > 0;
        long randomNumber = RandomUtils.nextLong(1, filesInDirectory + 1);
        return FileManager.fileManager().getGifFile(type.type(), String.valueOf(randomNumber));
    }
}
