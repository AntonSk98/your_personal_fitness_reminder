package ansk.development.domain;

/**
 * Enum that encapsulates all possible exercise types.
 *
 * @author Anton Skripin
 */
public enum ExerciseType {

    BACK("back"),
    BICEPS("biceps"),
    CHEST("chest"),
    LEGS("legs"),
    SHOULDERS("shoulders"),
    TRICEPS("triceps"),
    STRETCHING("stretching"),
    WEIGHT_FREE("weight_free"),
    PUSH_UPS("push_ups"),
    ABS("abs");


    private final String type;

    ExerciseType(String type) {
        this.type = type;
    }

    public String type() {
        return type;
    }
}
