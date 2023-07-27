package ansk.development.repository;

import ansk.development.repository.api.IWorkoutProcessRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * Implementation of {@link IWorkoutProcessRepository}.
 *
 * @author Anton Skripin
 */
public class WorkoutProcessRepository implements IWorkoutProcessRepository {

    private static WorkoutProcessRepository workoutProcessRepository;
    private final Map<String, Future<?>> RUNNING_PROCESSES = new HashMap<>();

    private WorkoutProcessRepository() {
    }

    public static WorkoutProcessRepository getRepository() {
        if (workoutProcessRepository == null) {
            workoutProcessRepository = new WorkoutProcessRepository();
        }

        return workoutProcessRepository;
    }

    @Override
    public void addRunningProcess(String chatId, Future<?> process) {
        if (this.RUNNING_PROCESSES.containsKey(chatId)) {
            throw new IllegalStateException("There is already a running process for chat id: " + chatId);
        }
        this.RUNNING_PROCESSES.put(chatId, process);
    }

    @Override
    public void removeRunningProcess(String chatId) {
        this.RUNNING_PROCESSES.remove(chatId);
    }

    @Override
    public boolean hasRunningProcesses(String chatId) {
        return RUNNING_PROCESSES.containsKey(chatId);
    }

    @Override
    public void interruptProcess(String chatId) {
        if (RUNNING_PROCESSES.containsKey(chatId)) {
            RUNNING_PROCESSES.get(chatId).cancel(true);
        }
    }
}
