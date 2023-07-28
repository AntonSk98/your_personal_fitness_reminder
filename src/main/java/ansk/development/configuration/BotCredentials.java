package ansk.development.configuration;

import java.util.Objects;

/**
 * Holds sensitive part loaded from the environment variables about the owner of a bot and the token to access it.
 *
 * @author Anton Skripin
 */
public class BotCredentials {

    private static final String PROPERTIES_CHAT_ID_ENV_VARIABLE_NAME = "CHAT_ID";
    private static final String PROPERTIES_TOKEN_ENV_VARIABLE_NAME = "TOKEN";
    private static final String PROPERTIES_TOKEN_USERNAME = "USERNAME";

    private final String chatId;
    private final String token;
    private final String username;

    public BotCredentials() {
        this.chatId = System.getenv(PROPERTIES_CHAT_ID_ENV_VARIABLE_NAME);
        this.token = System.getenv(PROPERTIES_TOKEN_ENV_VARIABLE_NAME);
        this.username = System.getenv(PROPERTIES_TOKEN_USERNAME);
        if (Objects.isNull(chatId) || Objects.isNull(token) || Objects.isNull(username)) {
            throw new IllegalStateException("Chat id or token or username is not provided!");
        }
    }

    public String getChatId() {
        return chatId;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }
}
