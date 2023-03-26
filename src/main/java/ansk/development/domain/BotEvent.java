package ansk.development.domain;

import ansk.development.exception.UnknownBotCommandException;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static ansk.development.domain.ConfigurationConstants.ALLOWED_USERNAMES;
import static ansk.development.domain.ConfigurationConstants.ALLOWED_CHAT_IDS;

/**
 * List of supported bot commands.
 */
public enum BotEvent {

    NEW_EXERCISE("/new_exercise"),
    SUPPRESS("/suppress_bot"),
    ENABLE("/enable_bot"),

    WARM_UP("/morning_routine"),

    UNKNOWN_USER("/unknown_user"),
    TOO_MANY_REQUESTS("/too_many_requests");

    private static final List<BotEvent> INTERNAL_COMMANDS = List.of(UNKNOWN_USER, TOO_MANY_REQUESTS);


    private final String command;

    BotEvent(String command) {
        this.command = command;
    }

    public static boolean isKnownCommand(Update update) {
        String message = update.getMessage().getText();
        for (BotEvent botEvent : BotEvent.values()) {
            if (botEvent.command().equals(message) && !INTERNAL_COMMANDS.contains(botEvent)) {
                return true;
            }
        }
        return false;
    }

    public static Optional<String> getChatId(Update update) {
        return Optional
                .ofNullable(update.getMessage())
                .map(Message::getChat)
                .map(Chat::getId)
                .map(Object::toString);
    }

    public static boolean isEvenFromPreviousDays(Update update) {
        Integer epochSecond = update.getMessage().getDate();
        return Instant
                .ofEpochSecond(epochSecond)
                .truncatedTo(ChronoUnit.DAYS)
                .isBefore(Instant.now().truncatedTo(ChronoUnit.DAYS));
    }

    public static boolean isFromAllowedUser(Update update) {
        return update.hasMessage()
                && Objects.nonNull(update.getMessage().getFrom())
                && Objects.nonNull(update.getMessage().getChat())
                && Arrays.asList(ALLOWED_USERNAMES).contains(update.getMessage().getFrom().getUserName())
                && Arrays.asList(ALLOWED_CHAT_IDS).contains(update.getMessage().getChatId().toString());
    }

    public static boolean isMessageWithText(Update update) {
        return update.hasMessage() && update.getMessage().hasText();
    }

    public static BotEvent toBotCommand(Update update) {
        String message = update.getMessage().getText();
        for (BotEvent botEvent : BotEvent.values()) {
            if (botEvent.command.equals(message)) {
                return botEvent;
            }
        }
        throw new UnknownBotCommandException();
    }

    public String command() {
        return command;
    }
}
