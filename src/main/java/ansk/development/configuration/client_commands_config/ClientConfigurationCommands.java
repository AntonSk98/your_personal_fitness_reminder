package ansk.development.configuration.client_commands_config;

import java.util.List;

/**
 * Contains client configuration commands to tune the properties of a bot.
 *
 * @author Anton Skripin
 */
public class ClientConfigurationCommands {
    private String message;
    private List<Command> commands;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Command> getCommands() {
        return commands;
    }

    public void setCommands(List<Command> commands) {
        this.commands = commands;
    }
}
