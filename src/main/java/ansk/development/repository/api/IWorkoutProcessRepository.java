package ansk.development.repository.api;

import java.util.concurrent.Future;

/**
 * Collects running requests for every user.
 *
 * @author Anton Skripin
 */
public interface IWorkoutProcessRepository {

    void addRunningProcess(String chatId, Future<?> process);
    void removeRunningProcess(String chatId);
    boolean hasRunningProcesses(String chatId);

    void interruptProcess(String chatId);
}
