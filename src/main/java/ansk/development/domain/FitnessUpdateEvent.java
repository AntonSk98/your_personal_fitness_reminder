package ansk.development.domain;

import ansk.development.configuration.ConfigRegistry;
import ansk.development.exception.UnknownBotCommandException;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

import static ansk.development.domain.FitnessBotCommands.*;
import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * Domain-specific extension of {@link Update} event.
 * It is enriched with the relevant helper methods to determine a correct handler for every received event.
 *
 * @author Anton Skripin
 */
public class FitnessUpdateEvent {
    private final Update update;

    public FitnessUpdateEvent(Update update) {
        this.update = update;
    }

    public Update getUpdate() {
        return update;
    }

    public boolean isPushUpsNotificationEvent() {
        return this.isKnownCommand() && this.toFitnessBotCommand().equals(PUSH_UPS);
    }

    public boolean isWeightFreeWorkoutEvent() {
        return this.isKnownCommand() && this.toFitnessBotCommand().equals(WEIGHT_FREE);
    }

    public boolean isResetTimerEvent() {
        return this.isKnownCommand() && this.toFitnessBotCommand().equals(RESET_TIMER);
    }

    public boolean isDisableNotificationsEvent() {
        return this.isKnownCommand() && this.toFitnessBotCommand().equals(DISABLE_FITNESS_REMINDERS);
    }

    public boolean isEnableNotificationEvent() {
        return this.isKnownCommand() && this.toFitnessBotCommand().equals(ENABLE_FITNESS_REMINDERS);
    }

    public boolean isStretchingWorkoutEvent() {
        return this.isKnownCommand() && STRETCHING.equals(this.toFitnessBotCommand());
    }

    public boolean isWorkoutWithDumbbellsEvent() {
        return this.isKnownCommand() && WITH_DUMBBELLS.equals(this.toFitnessBotCommand());
    }

    public boolean isStopWorkoutEvent() {
        return this.isKnownCommand() && STOP_WORKOUT.equals(this.toFitnessBotCommand());
    }

    public boolean isAbsWorkout() {
        return this.isKnownCommand() && ABS.equals(this.toFitnessBotCommand());
    }

    public boolean isChatIdPresent() {
        return this.getOptionalChatId().isPresent();
    }

    public String getChatId() {
        return this.getOptionalChatId().orElseThrow(NoSuchElementException::new);
    }

    public String getUsername() {
        return Optional.ofNullable(update.getMessage()).map(Message::getFrom).map(User::getUserName).orElse(EMPTY);
    }

    public boolean isEventFromPreviousDays() {
        Integer epochSecond = update.getMessage().getDate();
        return Instant.ofEpochSecond(epochSecond).truncatedTo(ChronoUnit.DAYS).isBefore(Instant.now().truncatedTo(ChronoUnit.DAYS));
    }

    public boolean isEventFromUnknownUser() {
        return !isFromAllowedUser();
    }

    public boolean isUnknownCommand() {
        return !isKnownCommand();
    }

    private boolean isKnownCommand() {
        String message = update.getMessage().getText();
        for (FitnessBotCommands botEvent : values()) {
            if (botEvent.command().equals(message)) {
                return true;
            }
        }
        return false;
    }

    private Optional<String> getOptionalChatId() {
        return Optional.ofNullable(update.getMessage()).map(Message::getChat).map(Chat::getId).map(Object::toString);
    }

    private boolean isFromAllowedUser() {
        return update.hasMessage() && Objects.nonNull(update.getMessage().getChat()) && ConfigRegistry
                .props()
                .forBot()
                .getAllowedChatIds()
                .contains(this.getChatId());
    }

    private FitnessBotCommands toFitnessBotCommand() {
        String message = update.getMessage().getText();
        for (FitnessBotCommands botEvent : values()) {
            if (botEvent.command().equals(message)) {
                return botEvent;
            }
        }
        throw new UnknownBotCommandException();
    }
}
