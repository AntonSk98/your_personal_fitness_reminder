package ansk.development.configuration;

import java.util.List;

/**
 * Core configuration property file with necessary props to start this Telegram bot.
 *
 * @author Anton Skripin
 */
public class FitnessBotProperties {
    private String username;
    private boolean onStartupNotificationsEnabled;
    private boolean ignoreOldEvents;
    private int sendExerciseDelayInMs;
    private int groupIntervalDelayInMs;
    private String creator;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

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

    public int getGroupIntervalDelayInMs() {
        return groupIntervalDelayInMs;
    }

    public void setGroupIntervalDelayInMs(int groupIntervalDelayInMs) {
        this.groupIntervalDelayInMs = groupIntervalDelayInMs;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
}
