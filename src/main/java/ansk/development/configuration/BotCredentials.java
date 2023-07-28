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

    private final String chatId;
    private final String token;

    public BotCredentials() {
        this.chatId = System.getenv(PROPERTIES_CHAT_ID_ENV_VARIABLE_NAME);
        this.token = System.getenv(PROPERTIES_TOKEN_ENV_VARIABLE_NAME);
        if (Objects.isNull(chatId) || Objects.isNull(token)) {
            throw new IllegalStateException("Chat id or token is not provided!");
        }
    }

    public String getChatId() {
        return chatId;
    }

    public String getToken() {
        return token;
    }
}
