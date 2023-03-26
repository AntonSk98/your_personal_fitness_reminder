package ansk.development.domain;

/**
 * All grouped exercise types are collected here.
 */
public enum ExerciseType {

    BACK("back"),
    BICEPS("biceps"),
    CHEST("chest"),
    LEGS("legs"),
    SHOULDERS("shoulders"),
    TRICEPS("triceps"),

    WARM_UP_AND_STRETCHING("warm up and stretching");


    private final String type;

    ExerciseType(String type) {
        this.type = type;
    }

    public String type() {
        return type;
    }
}
