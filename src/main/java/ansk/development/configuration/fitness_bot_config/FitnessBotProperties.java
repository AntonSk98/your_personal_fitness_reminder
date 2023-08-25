package ansk.development.configuration.fitness_bot_config;

/**
 * Core configuration property file with necessary props to start this Telegram bot.
 *
 * @author Anton Skripin
 */
public class FitnessBotProperties {
    private boolean onStartupNotificationsEnabled;
    private boolean ignoreOldEvents;
    private int sendExerciseDelayInMs;
    private String creator;
    private String timezone;

    private FitnessBotCredentials fitnessBotCredentials;


    public boolean isOnStartupNotificationsEnabled() {
        return onStartupNotificationsEnabled;
    }

    public void setOnStartupNotificationsEnabled(boolean onStartupNotificationsEnabled) {
        this.onStartupNotificationsEnabled = onStartupNotificationsEnabled;
    }

    public boolean isIgnoreOldEvents() {
        return ignoreOldEvents;
    }

    public void setIgnoreOldEvents(boolean ignoreOldEvents) {
        this.ignoreOldEvents = ignoreOldEvents;
    }

    public int getSendExerciseDelayInMs() {
        return sendExerciseDelayInMs;
    }

    public void setSendExerciseDelayInMs(int sendExerciseDelayInMs) {
        this.sendExerciseDelayInMs = sendExerciseDelayInMs;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getChatId() {
        return fitnessBotCredentials.getChatId();
    }

    public String getToken() {
        return fitnessBotCredentials.getToken();
    }

    public String getUsername() {
        return fitnessBotCredentials.getUsername();
    }

    public void setFitnessBotCredentials(FitnessBotCredentials fitnessBotCredentials) {
        this.fitnessBotCredentials = fitnessBotCredentials;
    }
}
