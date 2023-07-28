package ansk.development.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

/**
 * Encapsulates all the configuration properties.
 *
 * @author Anton Skripin
 */
public class ConfigRegistry {

    private static final String PROPERTIES_PATH_ENV_VARIABLE_NAME = "PATH_TO_CONFIG";

    private static final String PATH_TO_CONFIG = System.getenv(PROPERTIES_PATH_ENV_VARIABLE_NAME);
    private static ConfigRegistry configRegistry;

    private final BotCredentials botCredentials;
    private final FitnessBotProperties fitnessBotProperties;
    private final NotificationsProperties notificationsProperties;
    private final WorkoutSizeProperties workoutSizeProperties;
    private final ScheduledJobsProperties scheduledJobsProperties;
    private final ExerciseTypeCatalogSize exerciseTypeCatalogSize;

    private ConfigRegistry(BotCredentials botCredentials,
                           FitnessBotProperties fitnessBotProperties,
                           NotificationsProperties notificationsProperties,
                           WorkoutSizeProperties workoutSizeProperties,
                           ScheduledJobsProperties scheduledJobsProperties,
                           ExerciseTypeCatalogSize exerciseTypeCatalogSize) {
        this.botCredentials = botCredentials;
        this.fitnessBotProperties = fitnessBotProperties;
        this.notificationsProperties = notificationsProperties;
        this.workoutSizeProperties = workoutSizeProperties;
        this.scheduledJobsProperties = scheduledJobsProperties;
        this.exerciseTypeCatalogSize = exerciseTypeCatalogSize;
    }

    public static ConfigRegistry props() {
        if (configRegistry == null) {
            configRegistry = new ConfigRegistry(
                    new BotCredentials(),
                    initializeFitnessBotProperties(),
                    initializeNotificationProperties(),
                    initializeWorkoutSizeProperties(),
                    initializeScheduledJobsProperties(),
                    initializeTypeCatalogSize());
        }
        return configRegistry;
    }

    private static ExerciseTypeCatalogSize initializeTypeCatalogSize() {
        return toObjectProperty("exercise_type_catalog_size", ExerciseTypeCatalogSize.class);
    }

    private static ScheduledJobsProperties initializeScheduledJobsProperties() {
        return toObjectProperty("scheduled_jobs", ScheduledJobsProperties.class);
    }

    private static NotificationsProperties initializeNotificationProperties() {
        return toObjectProperty("notifications", NotificationsProperties.class);
    }

    private static FitnessBotProperties initializeFitnessBotProperties() {
        return toObjectProperty("fitness_bot", FitnessBotProperties.class);
    }

    private static WorkoutSizeProperties initializeWorkoutSizeProperties() {
        return toObjectProperty("workout_size", WorkoutSizeProperties.class);
    }

    private static <T> T toObjectProperty(String key, Class<T> clazz) {
        try {
            Yaml yaml = new Yaml();
            InputStream inputStream = new FileInputStream(PATH_TO_CONFIG);
            Map<String, Object> parsedConfig = yaml.load(inputStream);
            return new ObjectMapper().convertValue(parsedConfig.get(key), clazz);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(String.format("File does not exists under the following path: %s", PATH_TO_CONFIG), e);
        }
    }

    public BotCredentials botCredentials() {
        return botCredentials;
    }

    public FitnessBotProperties forBot() {
        return fitnessBotProperties;
    }

    public NotificationsProperties forNotification() {
        return notificationsProperties;
    }

    public ScheduledJobsProperties forScheduled() {
        return scheduledJobsProperties;
    }

    public WorkoutSizeProperties workoutSizes() {
        return workoutSizeProperties;
    }

    public ExerciseTypeCatalogSize exerciseSizes() {return exerciseTypeCatalogSize;}
}
