package ansk.development.configuration.scheduled_jobs_config;

import java.time.LocalTime;

/**
 * Configuration properties when no reminders should be sent.
 *
 * @author Anton Skripin
 */
public class NoReminders {
    private LocalTime from;
    private LocalTime to;

    public LocalTime getFrom() {
        return from;
    }

    public void setFrom(LocalTime from) {
        this.from = from;
    }

    public LocalTime getTo() {
        return to;
    }

    public void setTo(LocalTime to) {
        this.to = to;
    }
}
