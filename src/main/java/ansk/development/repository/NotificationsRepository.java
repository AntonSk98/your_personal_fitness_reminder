package ansk.development.repository;

import ansk.development.configuration.ConfigRegistry;
import ansk.development.repository.api.INotificationsRepository;

/**
 * Implementation of {@link INotificationsRepository}.
 *
 * @author Anton Skripin
 */
public class NotificationsRepository implements INotificationsRepository {

    private final Notification notification;

    private static NotificationsRepository notificationsRepository;

    private NotificationsRepository() {
        this.notification = new Notification(ConfigRegistry.props().forBot().getChatId(), true);
    }

    public static NotificationsRepository getRepository() {
        if (notificationsRepository == null) {
            notificationsRepository = new NotificationsRepository();
        }

        return notificationsRepository;
    }

    private void changeNotificationStatusForUser(String chatId, boolean enabled) {
        notification.setNotificationsEnabled(chatId, enabled);
    }

    public String getNotifiedChatId() {
        return notification.getChatId();
    }

    public boolean areNotificationsEnabled(String chatId) {
        return notification.areNotificationsEnabled(chatId);
    }

    public void enableNotificationsForUser(String chatId) {
        changeNotificationStatusForUser(chatId, true);
    }

    public void disableNotificationsForUser(String chatId) {
        changeNotificationStatusForUser(chatId, false);
    }

    static class Notification {
        private final String chatId;
        private boolean notificationsEnabled;

        public Notification(String chatId, boolean notificationsEnabled) {
            this.chatId = chatId;
            this.notificationsEnabled = notificationsEnabled;
        }

        public String getChatId() {
            return chatId;
        }

        public boolean areNotificationsEnabled(String chatId) {
            if (!this.chatId.equals(chatId)) {
                throw new IllegalStateException(String.format("Unauthorized user %s is attending to get notification info for %s", chatId, this.chatId));
            }
            return this.notificationsEnabled;
        }

        public void setNotificationsEnabled(String chatId, boolean notificationsEnabled) {
            if (!this.chatId.equals(chatId)) {
                throw new IllegalStateException(String.format("Unauthorized user %s is attending to change notification policy for %s", chatId, this.chatId));
            }
            this.notificationsEnabled = notificationsEnabled;
        }
    }
}
