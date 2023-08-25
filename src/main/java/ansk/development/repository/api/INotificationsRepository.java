package ansk.development.repository.api;

/**
 * Collects the status of notifications for every user.
 *
 * @author Anton Skripin
 */
public interface INotificationsRepository {
    String getNotifiedChatId();

    boolean areNotificationsEnabled(String chatId);

    void enableNotificationsForUser(String chatId);

    void disableNotificationsForUser(String chatId);
}
