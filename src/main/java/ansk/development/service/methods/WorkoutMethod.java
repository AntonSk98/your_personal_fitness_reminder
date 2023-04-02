package ansk.development.service.methods;

import ansk.development.configuration.ConfigRegistry;
import ansk.development.domain.ExerciseType;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static ansk.development.service.ExerciseAccessManager.getRandomExercise;

/**
 * Class that generates a set of exercises for a client.
 * Depending on the way, a caller constructs the object of this class, different workout types are generated.
 *
 * @author Anton Skripin
 */
public class WorkoutMethod extends AbstractMethod {

    private final List<SendAnimation> exercises = new ArrayList<>();

    private WorkoutMethod(String chatId) {
        super(chatId);
    }

    public static WorkoutMethod generateWorkout(String chatId) {
        return new WorkoutMethod(chatId);
    }

    public WorkoutMethod withDumbbells() {
        exercises.addAll(generateWorkoutWithDumbbells());
        return this;
    }

    public WorkoutMethod weightFreeWorkout() {
        exercises.addAll(generateWeightFreeWorkout());
        return this;
    }

    public WorkoutMethod pushUpsWorkout() {
        exercises.addAll(generatePushUpsWorkout());
        return this;
    }

    public WorkoutMethod absWorkout() {
        exercises.addAll(generateAbsWorkout());
        return this;
    }

    public WorkoutMethod stretchingWorkout() {
        exercises.addAll(generateStretchingWorkout());
        return this;
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
        return toAnimation(ExerciseType.STRETCHING);
    }

    private SendAnimation getRandomChestExercise() {
        return toAnimation(ExerciseType.CHEST);
    }

    private SendAnimation getRandomWeightFreeExercise() {
        return toAnimation(ExerciseType.WEIGHT_FREE);
    }

    private SendAnimation getRandomAbsExercise() {
        return toAnimation(ExerciseType.ABS);
    }

    private SendAnimation getRandomPushUpExercise() {
        return toAnimation(ExerciseType.PUSH_UPS);
    }

    private SendAnimation toAnimation(ExerciseType type) {
        InputFile inputFile = getRandomExercise(type);
        SendAnimation sendAnimation = new SendAnimation(this.getChatId(), inputFile);
        sendAnimation.setCaption(String.format("Exercise for: %s", type.type()));
        return sendAnimation;
    }

    public SendAnimation[] getExercises() {
        return exercises.toArray(SendAnimation[]::new);
    }

    private List<SendAnimation> generateStretchingWorkout() {
        return generateWorkout(
                this::getRandomMorningRoutineExercise,
                ConfigRegistry.props().workoutSizes().getStretchingWorkout());
    }

    private List<SendAnimation> generateWeightFreeWorkout() {
        return generateWorkout(
                this::getRandomWeightFreeExercise,
                ConfigRegistry.props().workoutSizes().getWeightFree());
    }

    private List<SendAnimation> generatePushUpsWorkout() {
        return generateWorkout(
                this::getRandomPushUpExercise,
                ConfigRegistry.props().workoutSizes().getPushUps());
    }

    private List<SendAnimation> generateAbsWorkout() {
        return generateWorkout(
                this::getRandomAbsExercise,
                ConfigRegistry.props().workoutSizes().getAbsWorkout());
    }

    private List<SendAnimation> generateWorkout(Supplier<SendAnimation> randomExerciseGenerator, int numberOfExercises) {
        List<SendAnimation> workout = new ArrayList<>();
        while (workout.size() < numberOfExercises) {
            SendAnimation generatedMorningExercise = randomExerciseGenerator.get();
            if (workout
                    .stream()
                    .anyMatch(sendAnimation -> sendAnimation
                            .getAnimation()
                            .getMediaName()
                            .equals(generatedMorningExercise
                                    .getAnimation()
                                    .getMediaName()))) {
                continue;
            }
            workout.add(generatedMorningExercise);
        }
        return workout;
    }

    private List<SendAnimation> generateWorkoutWithDumbbells() {
        List<SendAnimation> workout = new ArrayList<>();

        for (int iteration = 0; iteration < ConfigRegistry.props().workoutSizes().getWorkoutWithDumbbells(); iteration++) {
            workout.addAll(
                    List.of(
                            getRandomBicepsExercise(),
                            getRandomTricepsExercise(),
                            getRandomChestExercise(),
                            getRandomShoulderExercise(),
                            getRandomBackExercise(),
                            getRandomLegsExercise()
                    )
            );
        }
        return workout;
    }
}
