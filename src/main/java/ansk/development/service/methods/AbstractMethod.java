package ansk.development.service.methods;

/**
 * Common method class that creates a method that is to be sent to a client.
 *
 * @author Anton SKripin
 */
public abstract class AbstractMethod {

    private final String chatId;


    public AbstractMethod(String chatId) {
        this.chatId = chatId;
    }

    public String getChatId() {
        return chatId;
    }
}
