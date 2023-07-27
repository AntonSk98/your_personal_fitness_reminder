package ansk.development.domain;

/**
 * Encapsulates the lsit of supported bot commands.
 *
 * @author Anton Skripin
 */
public enum FitnessBotCommands {

    WITH_DUMBBELLS("/with_dumbbells"),
    DISABLE_NOTIFICATIONS("/disable_notifications"),
    ENABLE_NOTIFICATIONS("/enable_notifications"),
    STRETCHING("/stretching"),
    WEIGHT_FREE("/weight_free"),
    RESET_TIMER("/reset_notifications_timer"),
    PUSH_UPS("/push_ups"),
    ABS("/abs"),
    STOP_WORKOUT("/stop_workout");

    private final String command;

    FitnessBotCommands(String command) {
        this.command = command;
    }

    public String command() {
        return command;
    }
}
