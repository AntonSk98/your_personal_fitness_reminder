package ansk.development.repository;

import ansk.development.configuration.ConfigRegistry;
import ansk.development.repository.api.IWorkoutProcessRepository;

import java.util.Objects;
import java.util.concurrent.Future;

/**
 * Implementation of {@link IWorkoutProcessRepository}.
 *
 * @author Anton Skripin
 */
public class WorkoutProcessRepository implements IWorkoutProcessRepository {

    private static WorkoutProcessRepository workoutProcessRepository;
    private final RunningProcess runningProcess;

    private WorkoutProcessRepository() {
        this.runningProcess = new RunningProcess(ConfigRegistry.props().forBot().getChatId());
    }

    public static WorkoutProcessRepository getRepository() {
        if (workoutProcessRepository == null) {
            workoutProcessRepository = new WorkoutProcessRepository();
        }

        return workoutProcessRepository;
    }

    @Override
    public void addRunningProcess(String chatId, Future<?> process) {
        runningProcess.addRunningProcess(chatId, process);
    }

    @Override
    public void removeRunningProcess(String chatId) {
        runningProcess.removeRunningProcess(chatId);
    }

    @Override
    public boolean hasRunningProcesses(String chatId) {
        return runningProcess.hasRunningProcesses(chatId);
    }

    @Override
    public void interruptProcess(String chatId) {
        runningProcess.interruptRunningProcess(chatId);
    }

    private class RunningProcess {
        private String chatId;
        private Future<?> runningProcess;

        public RunningProcess(String chatId) {
            this.chatId = chatId;
        }

        public void addRunningProcess(String chatId, Future<?> scheduledFuture) {
            if (!this.chatId.equals(chatId)) {
                throw new IllegalStateException();
            }
            this.runningProcess = scheduledFuture;
        }

        public void removeRunningProcess(String chatId) {
            if (!this.chatId.equals(chatId)) {
                throw new IllegalStateException();
            }
            this.runningProcess = null;
        }

        public boolean hasRunningProcesses(String chatId) {
            return this.chatId.equals(chatId) && Objects.nonNull(this.runningProcess);
        }

        public void interruptRunningProcess(String chatId) {
            if (hasRunningProcesses(chatId)) {
                this.runningProcess.cancel(true);
            }
        }
    }
}
