package ansk.development.repository;

import ansk.development.configuration.ConfigRegistry;
import ansk.development.repository.api.INotificationsRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of {@link INotificationsRepository}.
 *
 * @author Anton Skripin
 */
public class NotificationsRepository implements INotificationsRepository {

    private static final List<Notification> NOTIFICATION_REGISTRY = ConfigRegistry
            .props()
            .forBot()
            .getAllowedChatIds()
            .stream()
            .map(chatId -> new Notification(chatId, true))
            .collect(Collectors.toList());
    private static NotificationsRepository notificationsRepository;

    private NotificationsRepository() {
    }

    public static NotificationsRepository getRepository() {
        if (notificationsRepository == null) {
            notificationsRepository = new NotificationsRepository();
        }

        return notificationsRepository;
    }

    private static void changeNotificationStatusForUser(String chatId, boolean enabled) {
        NOTIFICATION_REGISTRY
                .stream()
                .filter(notification -> notification.getChatId().equals(chatId))
                .forEach(notification -> notification.setNotificationsEnabled(enabled));
    }

    public List<String> getAllChatIds() {
        return NOTIFICATION_REGISTRY.stream().map(Notification::getChatId).collect(Collectors.toList());
    }

    public List<String> getAllChatIdsWithEnabledNotifications() {
        return NOTIFICATION_REGISTRY
                .stream()
                .filter(Notification::isNotificationsEnabled)
                .map(Notification::getChatId)
                .collect(Collectors.toList());
    }

    public boolean areNotificationsEnabled(String chatId) {
        return NOTIFICATION_REGISTRY
                .stream()
                .filter(notification -> notification.getChatId().equals(chatId))
                .filter(Notification::isNotificationsEnabled)
                .map(Notification::isNotificationsEnabled)
                .findFirst()
                .orElse(false);
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

        public boolean isNotificationsEnabled() {
            return notificationsEnabled;
        }

        public void setNotificationsEnabled(boolean notificationsEnabled) {
            this.notificationsEnabled = notificationsEnabled;
        }
    }
}
