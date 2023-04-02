package ansk.development.repository.api;

/**
 * Collects running requests for every user.
 *
 * @author Anton Skripin
 */
public interface IWorkoutProcessRepository {

    void addChatToRunningWorkoutProcesses(String chatId);
    void removeChatFromRunningWorkoutProcesses(String chatId);
    boolean hasRunningProcesses(String chatId);
}
