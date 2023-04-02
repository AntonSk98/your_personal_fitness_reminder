package ansk.development.configuration;

/**
 * Notification-related properties to inform user about certain events.
 *
 * @author Anton Skripin
 */
public class NotificationsProperties {
    private String workoutWithDumbbells;
    private String stretchingWorkout;
    private String weightFree;
    private String absWorkout;
    private String toUnknownUser;
    private String aboutUnknownUser;
    private String unknownCommand;
    private String enabledNotifications;
    private String disabledNotifications;
    private String onStartup;
    private String workoutInProgress;
    private String successfullyResetTimer;
    private String failedToResetTimer;
    private String pushUps;

    public String getWorkoutWithDumbbells() {
        return workoutWithDumbbells;
    }

    public void setWorkoutWithDumbbells(String workoutWithDumbbells) {
        this.workoutWithDumbbells = workoutWithDumbbells;
    }

    public String getStretchingWorkout() {
        return stretchingWorkout;
    }

    public void setStretchingWorkout(String stretchingWorkout) {
        this.stretchingWorkout = stretchingWorkout;
    }

    public String getWeightFree() {
        return weightFree;
    }

    public void setWeightFree(String weightFree) {
        this.weightFree = weightFree;
    }

    public String getAbsWorkout() {
        return absWorkout;
    }

    public void setAbsWorkout(String absWorkout) {
        this.absWorkout = absWorkout;
    }

    public String getToUnknownUser() {
        return toUnknownUser;
    }

    public void setToUnknownUser(String toUnknownUser) {
        this.toUnknownUser = toUnknownUser;
    }

    public String getAboutUnknownUser() {
        return aboutUnknownUser;
    }

    public void setAboutUnknownUser(String aboutUnknownUser) {
        this.aboutUnknownUser = aboutUnknownUser;
    }

    public String getUnknownCommand() {
        return unknownCommand;
    }

    public void setUnknownCommand(String unknownCommand) {
        this.unknownCommand = unknownCommand;
    }

    public String getEnabledNotifications() {
        return enabledNotifications;
    }

    public void setEnabledNotifications(String enabledNotifications) {
        this.enabledNotifications = enabledNotifications;
    }

    public String getDisabledNotifications() {
        return disabledNotifications;
    }

    public void setDisabledNotifications(String disabledNotifications) {
        this.disabledNotifications = disabledNotifications;
    }

    public String getOnStartup() {
        return onStartup;
    }

    public void setOnStartup(String onStartup) {
        this.onStartup = onStartup;
    }

    public String getWorkoutInProgress() {
        return workoutInProgress;
    }

    public void setWorkoutInProgress(String workoutInProgress) {
        this.workoutInProgress = workoutInProgress;
    }

    public String getSuccessfullyResetTimer() {
        return successfullyResetTimer;
    }

    public void setSuccessfullyResetTimer(String successfullyResetTimer) {
        this.successfullyResetTimer = successfullyResetTimer;
    }

    public String getFailedToResetTimer() {
        return failedToResetTimer;
    }

    public void setFailedToResetTimer(String failedToResetTimer) {
        this.failedToResetTimer = failedToResetTimer;
    }

    public String getPushUps() {
        return pushUps;
    }

    public void setPushUps(String pushUps) {
        this.pushUps = pushUps;
    }
}
