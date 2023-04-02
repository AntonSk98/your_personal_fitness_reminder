package ansk.development.service.methods;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

/**
 * Class to generate a fitness bot message to notify a client.
 *
 * @author Anton Skripin
 */
public class MessageMethod extends AbstractMethod {

    private final String message;

    public MessageMethod(String chatId, String message) {
        super(chatId);
        this.message = message;
    }

    public SendMessage getMessage() {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(getChatId());
        sendMessage.setText(this.message);
        return sendMessage;
    }
}
