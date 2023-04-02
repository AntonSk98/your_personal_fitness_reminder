package ansk.development.repository;

import ansk.development.repository.api.IWorkoutProcessRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link IWorkoutProcessRepository}.
 *
 * @author Anton Skripin
 */
public class WorkoutProcessRepository implements IWorkoutProcessRepository {

    private static WorkoutProcessRepository workoutProcessRepository;

    private WorkoutProcessRepository() {
    }

    public static WorkoutProcessRepository getRepository() {
        if (workoutProcessRepository == null) {
            workoutProcessRepository = new WorkoutProcessRepository();
        }

        return workoutProcessRepository;
    }

    private final List<String> CHATS_WITH_RUNNING_PROCESSES = new ArrayList<>();

    @Override
    public void addChatToRunningWorkoutProcesses(String chatId) {
        this.CHATS_WITH_RUNNING_PROCESSES.add(chatId);
    }

    @Override
    public void removeChatFromRunningWorkoutProcesses(String chatId) {
        this.CHATS_WITH_RUNNING_PROCESSES.remove(chatId);
    }

    @Override
    public boolean hasRunningProcesses(String chatId) {
        return CHATS_WITH_RUNNING_PROCESSES.contains(chatId);
    }
}
