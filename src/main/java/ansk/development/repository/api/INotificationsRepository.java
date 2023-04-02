package ansk.development.repository.api;

import java.util.List;

/**
 * Collects the status of notifications for every user.
 *
 * @author Anton Skripin
 */
public interface INotificationsRepository {
    List<String> getAllChatIdsWithEnabledNotifications();
    boolean areNotificationsEnabled(String chatId);
    void enableNotificationsForUser(String chatId);
    void disableNotificationsForUser(String chatId);
}
