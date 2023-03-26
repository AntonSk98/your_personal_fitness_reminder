package ansk.development.service.api;

/**
 * Common service class that generates an action.
 */
public abstract class AbstractGenerator {

    private final String chatId;


    public AbstractGenerator(String chatId) {
        this.chatId = chatId;
    }

    public String getChatId() {
        return chatId;
    }
}
