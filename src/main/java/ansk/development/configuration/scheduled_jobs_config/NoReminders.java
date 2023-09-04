package ansk.development.configuration.scheduled_jobs_config;

import ansk.development.configuration.ConfigRegistry;

import java.time.*;

/**
 * Configuration properties when no reminders should be sent.
 *
 * @author Anton Skripin
 */
public class NoReminders {
    private LocalTime from;
    private LocalTime to;

    /**
     * Converts local time set in technical configuration to zoned date time.
     * <p>
     * Zone is taken from the technical configuration.
     * <p>
     *
     * @return {@link #from} in {@link  ZonedDateTime}
     */
    public ZonedDateTime getFromInUserTimeZone() {
        return ZonedDateTime.of(this.from.atDate(LocalDate.now()),
                ZoneId.of(ConfigRegistry.props().botProperties().getTimezone()));
    }

    public void setFrom(LocalTime from) {
        this.from = from;
    }

    /**
     * Converts local time set in technical configuration to zoned date time.
     * <p>
     * Zone is taken from the technical configuration.
     * <p>
     * Date conversion example is the following:
     * From 9 to 15 -> no need to add extra day
     * From 15 to 9 -> must add one extra data since 9.00 is after than 15.00
     *
     * @return {@link #to} in {@link  ZonedDateTime}
     */
    public ZonedDateTime getToInUserTimeZone() {
        // example: from 9 to 15 -> no need to add a day
        // from 15 to 9 -> must add plus 1 day to "TO"
        LocalDateTime localDateTime = this
                .to
                .atDate(LocalDate.now())
                .plusDays(from.getHour() - to.getHour() > 0 ? 1 : 0);

        return ZonedDateTime.of(localDateTime, ZoneId.of(ConfigRegistry.props().botProperties().getTimezone()));
    }

    public void setTo(LocalTime to) {
        this.to = to;
    }
}
