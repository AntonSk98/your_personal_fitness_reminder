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
        ZoneId fromTechnicalConfiguration = ZoneId.of(ConfigRegistry.props().botProperties().getTimezone());

        boolean isAm = LocalTime.now(fromTechnicalConfiguration).getHour() < 12;
        boolean inTheSameDay = from.getHour() < to.getHour();

        LocalDateTime localTime = this
                .from
                .atDate(LocalDate.now())
                .minusDays(isAm && !inTheSameDay ? 1 : 0);

        return ZonedDateTime.of(localTime, fromTechnicalConfiguration);
    }

    public void setFrom(LocalTime from) {
        this.from = from;
    }

    /**
     * Converts local time set in technical configuration to zoned date time.
     * <p>
     * Zone is taken from the technical configuration.
     *
     * @return {@link #to} in {@link  ZonedDateTime}
     */
    public ZonedDateTime getToInUserTimeZone() {
        ZoneId fromTechnicalConfiguration = ZoneId.of(ConfigRegistry.props().botProperties().getTimezone());

        boolean isPm = LocalTime.now(fromTechnicalConfiguration).getHour() > 12;

        boolean inTheSameDay = from.getHour() < to.getHour();

        LocalDateTime localDateTime = this
                .to
                .atDate(LocalDate.now())
                .plusDays(isPm && !inTheSameDay ? 1 : 0);

        return ZonedDateTime.of(localDateTime, fromTechnicalConfiguration);
    }

    public void setTo(LocalTime to) {
        this.to = to;
    }
}
