package ansk.development.configuration.client_commands_config;

/**
 * Contains specific configuration command to tune the behavior of a fitness bot.
 *
 * @author Anton Skripim
 */
public class Command {
    private String description;
    private String command;
    private String example;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }
}
