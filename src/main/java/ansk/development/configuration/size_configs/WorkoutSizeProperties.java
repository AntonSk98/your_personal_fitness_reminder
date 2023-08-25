package ansk.development.configuration.size_configs;

/**
 * Properties that configure the number of exercises in every available workout type.
 *
 * @author Anton Skripin
 */
public class WorkoutSizeProperties {
    private int workoutWithDumbbells;
    private int stretchingWorkout;
    private int weightFree;
    private int pushUps;
    private int absWorkout;

    public int getWorkoutWithDumbbells() {
        return workoutWithDumbbells;
    }

    public void setWorkoutWithDumbbells(int workoutWithDumbbells) {
        this.workoutWithDumbbells = workoutWithDumbbells;
    }

    public int getStretchingWorkout() {
        return stretchingWorkout;
    }

    public void setStretchingWorkout(int stretchingWorkout) {
        this.stretchingWorkout = stretchingWorkout;
    }

    public int getWeightFree() {
        return weightFree;
    }

    public void setWeightFree(int weightFree) {
        this.weightFree = weightFree;
    }

    public int getPushUps() {
        return pushUps;
    }

    public void setPushUps(int pushUps) {
        this.pushUps = pushUps;
    }

    public int getAbsWorkout() {
        return absWorkout;
    }

    public void setAbsWorkout(int absWorkout) {
        this.absWorkout = absWorkout;
    }
}
