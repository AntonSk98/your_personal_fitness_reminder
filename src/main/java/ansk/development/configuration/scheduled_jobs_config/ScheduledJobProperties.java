package ansk.development.configuration.scheduled_jobs_config;

/**
 * Properties for scheduled job routines.
 *
 * @author Anton Skripin
 */
public class ScheduledJobProperties {
    private int initialDelay;
    private int interval;

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
}
