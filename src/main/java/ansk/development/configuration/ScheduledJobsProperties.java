package ansk.development.configuration;

import java.util.concurrent.TimeUnit;

/**
 * Properties related to reoccurring scheduled jobs.
 *
 * @author Anton Skripin
 */
public class ScheduledJobsProperties {

    private ScheduledJobProperties checkSession;
    private ScheduledJobProperties sendWorkout;

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

    public static class ScheduledJobProperties{
        private int initialDelay;
        private int interval;
        private TimeUnit timeUnit;

        public int getInitialDelay() {
            return initialDelay;
        }

        public void setInitialDelay(int initialDelay) {
            this.initialDelay = initialDelay;
        }

        public int getInterval() {
            return interval;
        }

        public void setInterval(int interval) {
            this.interval = interval;
        }

        public TimeUnit getTimeUnit() {
            return timeUnit;
        }

        public void setTimeUnit(TimeUnit timeUnit) {
            this.timeUnit = timeUnit;
        }
    }
}
