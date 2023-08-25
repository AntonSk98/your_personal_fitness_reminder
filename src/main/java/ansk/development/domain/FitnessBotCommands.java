package ansk.development.domain;

/**
 * Encapsulates the lsit of supported bot commands.
 *
 * @author Anton Skripin
 */
public enum FitnessBotCommands {

    WITH_DUMBBELLS("/with_dumbbells"),
    DISABLE_FITNESS_REMINDERS("/disable_fitness_reminders"),
    ENABLE_FITNESS_REMINDERS("/enable_fitness_reminders"),
    STRETCHING("/stretching"),
    WEIGHT_FREE("/weight_free"),
    RESET_TIMER("/reset_notifications_timer"),
    PUSH_UPS("/push_ups"),
    ABS("/abs"),
    STOP_WORKOUT("/stop_workout"),

    SET_TIMEZONE("/set_timezone", 1);

    private final String command;
    private final int numberOfParameters;

    FitnessBotCommands(String command) {
        this.command = command;
        this.numberOfParameters = 0;
    }

    FitnessBotCommands(String command, int numberOfParameters) {
        this.command = command;
        this.numberOfParameters = numberOfParameters;
    }

    public String command() {
        return command;
    }

    public int numberOfParameters() {
        return this.numberOfParameters;
    }
}
