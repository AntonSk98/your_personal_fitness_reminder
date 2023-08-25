package ansk.development.configuration.scheduled_jobs_config;

/**
 * Properties related to scheduled routines.
 *
 * @author Anton Skripin
 */
public class ScheduledJobsProperties {

    private ScheduledJobProperties checkSession;
    private ScheduledJobProperties sendWorkout;
    private NoReminders noReminders;

    public ScheduledJobProperties getCheckSession() {
        return checkSession;
    }

    public void setCheckSession(ScheduledJobProperties checkSession) {
        this.checkSession = checkSession;
    }

    public ScheduledJobProperties getSendWorkout() {
        return sendWorkout;
    }

    public void setSendWorkout(ScheduledJobProperties sendWorkout) {
        this.sendWorkout = sendWorkout;
    }

    public NoReminders getNoReminders() {
        return noReminders;
    }

    public void setNoReminders(NoReminders noReminders) {
        this.noReminders = noReminders;
    }
}
