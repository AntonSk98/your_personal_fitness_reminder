package ansk.development.configuration.size_configs;

/**
 * Configuration property class that maps each type of exercise to its number available for workout.
 *
 * @author Anton Skripin
 */
public class ExerciseTypeCatalogSize {

    private int biceps;
    private int triceps;
    private int chest;
    private int back;
    private int shoulders;
    private int legs;
    private int stretching;
    private int weightFree;
    private int pushUps;
    private int abs;

    public int getBiceps() {
        return biceps;
    }

    public void setBiceps(int biceps) {
        this.biceps = biceps;
    }

    public int getTriceps() {
        return triceps;
    }

    public void setTriceps(int triceps) {
        this.triceps = triceps;
    }

    public int getChest() {
        return chest;
    }

    public void setChest(int chest) {
        this.chest = chest;
    }

    public int getBack() {
        return back;
    }

    public void setBack(int back) {
        this.back = back;
    }

    public int getShoulders() {
        return shoulders;
    }

    public void setShoulders(int shoulders) {
        this.shoulders = shoulders;
    }

    public int getLegs() {
        return legs;
    }

    public void setLegs(int legs) {
        this.legs = legs;
    }

    public int getStretching() {
        return stretching;
    }

    public void setStretching(int stretching) {
        this.stretching = stretching;
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

    public int getAbs() {
        return abs;
    }

    public void setAbs(int abs) {
        this.abs = abs;
    }
}
