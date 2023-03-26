package ansk.development.service.generators;

import ansk.development.domain.ExerciseType;
import ansk.development.domain.ExerciseTypeToQuantity;
import ansk.development.service.api.AbstractGenerator;
import ansk.development.utils.FileUtils;
import org.apache.commons.lang3.RandomUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that generates a set of exercise in a random way.
 * For every muscle group there will be generated one random exercise.
 */
public class ExerciseGenerator extends AbstractGenerator {

    private final List<SendAnimation> exerciseGifs = new ArrayList<>();

    public ExerciseGenerator(String chatId, boolean withDumbbells, boolean morningRoutine) {
        super(chatId);
        setContent(withDumbbells, morningRoutine);
    }

    private static InputFile getRandomExercise(ExerciseType type) {
        long filesInDirectory = ExerciseTypeToQuantity.getNumberByType(type);
        assert filesInDirectory > 0;
        long randomNumber = RandomUtils.nextLong(1, filesInDirectory + 1);
        return FileUtils.getGifFile(type.type(), String.valueOf(randomNumber));
    }

    private void setContent(boolean withDumbbells, boolean morningRoutine) {
        if (withDumbbells) {
            exerciseGifs.addAll(generateExerciseWithDumbbells());
            return;
        }
        if (morningRoutine) {
            exerciseGifs.addAll(generateMorningRoutineExercise());
        }
    }

    private SendAnimation getRandomBicepsExercise() {
        return toAnimation(ExerciseType.BICEPS);
    }

    private SendAnimation getRandomTricepsExercise() {
        return toAnimation(ExerciseType.TRICEPS);
    }

    private SendAnimation getRandomShoulderExercise() {
        return toAnimation(ExerciseType.SHOULDERS);
    }

    private SendAnimation getRandomLegsExercise() {
        return toAnimation(ExerciseType.LEGS);
    }

    private SendAnimation getRandomBackExercise() {
        return toAnimation(ExerciseType.BACK);
    }

    private SendAnimation getRandomMorningRoutineExercise() {
        return toAnimation(ExerciseType.WARM_UP_AND_STRETCHING);
    }

    private SendAnimation getRandomChestExercise() {
        return toAnimation(ExerciseType.CHEST);
    }

    private SendAnimation toAnimation(ExerciseType type) {
        InputFile inputFile = getRandomExercise(type);
        SendAnimation sendAnimation = new SendAnimation(this.getChatId(), inputFile);
        sendAnimation.setCaption(String.format("Exercise for: %s", type.type()));
        return sendAnimation;
    }

    public List<SendAnimation> getExerciseGifs() {
        return exerciseGifs;
    }

    private List<SendAnimation> generateMorningRoutineExercise() {
        List<SendAnimation> generatedExercises = new ArrayList<>();
        int exercisesToBeGenerated = 7;
        while (generatedExercises.size() < exercisesToBeGenerated) {
            SendAnimation generatedMorningExercise = getRandomMorningRoutineExercise();
            if (generatedExercises
                    .stream()
                    .anyMatch(sendAnimation -> sendAnimation
                            .getAnimation()
                            .getMediaName()
                            .equals(generatedMorningExercise
                                    .getAnimation()
                                    .getMediaName()))) {
                continue;
            }
            generatedExercises.add(generatedMorningExercise);
        }
        return generatedExercises;
    }

    private List<SendAnimation> generateExerciseWithDumbbells() {

        return List.of(
                getRandomBicepsExercise(),
                getRandomTricepsExercise(),
                getRandomChestExercise(),
                getRandomShoulderExercise(),
                getRandomBackExercise(),
                getRandomLegsExercise()
        );
    }
}
