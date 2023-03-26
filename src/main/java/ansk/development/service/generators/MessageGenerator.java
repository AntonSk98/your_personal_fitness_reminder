package ansk.development.service.generators;

import ansk.development.service.api.AbstractGenerator;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

/**
 * Class to generate a Telegram message.
 */
public class MessageGenerator extends AbstractGenerator {

    private final String message;

    public MessageGenerator(String message, String chatId) {
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
