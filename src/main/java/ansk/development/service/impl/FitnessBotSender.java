package ansk.development.service.impl;

import ansk.development.bot.FitnessReminderBot;
import ansk.development.configuration.ConfigRegistry;
import ansk.development.configuration.notification_config.NotificationsProperties;
import ansk.development.exception.FitnessBotOperationException;
import ansk.development.repository.WorkoutProcessRepository;
import ansk.development.repository.api.IWorkoutProcessRepository;
import ansk.development.service.api.IFitnessBotSender;
import ansk.development.service.methods.MessageMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.ResponseParameters;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Future;

/**
 * Implementation of {@link IFitnessBotSender}.
 *
 * @author Anton Skripin
 */
public class FitnessBotSender implements IFitnessBotSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(IFitnessBotSender.class);
    private static IFitnessBotSender fitnessBotResponseSender;
    private final IWorkoutProcessRepository processRepository;


    private FitnessBotSender() {
        this.processRepository = WorkoutProcessRepository.getRepository();
    }

    public static IFitnessBotSender getSender() {
        if (fitnessBotResponseSender == null) {
            fitnessBotResponseSender = new FitnessBotSender();
        }
        return fitnessBotResponseSender;
    }

    @Override
    public void sendMessage(SendMessage message) throws FitnessBotOperationException {
        try {
            FitnessReminderBot.getFitnessBot().execute(message);
        } catch (TelegramApiException e) {
            LOGGER.error("An error occurred while sending a message: {}", message, e);
            throw new FitnessBotOperationException(e);
        }
    }

    @Override
    public void sendWorkoutExercise(SendAnimation exercise) throws FitnessBotOperationException {
        try {
            FitnessReminderBot.getFitnessBot().execute(exercise);
        } catch (TelegramApiRequestException e) {
            onTooManyRequestsMessage(exercise.getChatId(), e.getParameters());
            throw new FitnessBotOperationException(e);
        } catch (TelegramApiException e) {
            LOGGER.error("An error occurred while sending an exercise: {}", exercise, e);
            throw new FitnessBotOperationException(e);
        }
    }

    @Override
    public void sendMessages(SendMessage... messages) throws FitnessBotOperationException {
        for (SendMessage sendMessage : messages) {
            sendMessage(sendMessage);
        }
    }

    @Override
    public void sendWorkoutAsRegisteredProcess(String chatId, SendAnimation... exercises) {
        registerProcess(chatId, TelegramFitnessExecutorService.executorService().executeAsync(() -> {
            for (SendAnimation exercise : exercises) {
                try {
                    sendWorkoutExercise(exercise);
                    Thread.sleep(ConfigRegistry.props().botProperties().getSendExerciseDelayInMs());
                } catch (FitnessBotOperationException e) {
                    LOGGER.error("Unexpected error occurred while sending workout. ChatID: {}", chatId);
                    break;
                } catch (InterruptedException e) {
                    LOGGER.warn("Running process was interrupted by another process");
                    break;
                }
            }
            unregisterProcess(chatId);
        }));

    }

    @Override
    public void onTooManyRequestsMessage(String chatId, ResponseParameters responseParameters) throws FitnessBotOperationException {
        Objects.requireNonNull(chatId);
        String retryAfterSeconds = Optional
                .ofNullable(responseParameters)
                .map(ResponseParameters::getRetryAfter)
                .map(String::valueOf)
                .orElse(ConfigRegistry.props().notifications().getDefaultSecondsPlaceholder());
        String templateNotification = ConfigRegistry.props().notifications().getTooManyRequest();
        MessageMethod messageMethod = new MessageMethod(chatId,
                NotificationsProperties.customizeTemplate(templateNotification, retryAfterSeconds));
        this.sendMessages(messageMethod.getMessage());
    }

    private void registerProcess(String chatId, Future<?> process) {
        this.processRepository.addRunningProcess(chatId, process);
    }

    private void unregisterProcess(String chatId) {
        this.processRepository.removeRunningProcess(chatId);
    }

}
