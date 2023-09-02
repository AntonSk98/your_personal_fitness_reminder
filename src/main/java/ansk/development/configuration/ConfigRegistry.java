package ansk.development.configuration;

import ansk.development.configuration.client_commands_config.ClientConfigurationCommands;
import ansk.development.configuration.fitness_bot_config.FitnessBotCredentials;
import ansk.development.configuration.fitness_bot_config.FitnessBotProperties;
import ansk.development.configuration.notification_config.NotificationsProperties;
import ansk.development.configuration.path_config.PathProperties;
import ansk.development.configuration.scheduled_jobs_config.ScheduledJobsProperties;
import ansk.development.configuration.size_configs.WorkoutSizeProperties;
import ansk.development.configuration.system_config.ObjectMapperConfiguration;
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
    private final FitnessBotProperties fitnessBotProperties;
    private final NotificationsProperties notificationsProperties;
    private final WorkoutSizeProperties workoutSizeProperties;
    private final ScheduledJobsProperties scheduledJobsProperties;
    private final PathProperties pathProperties;
    private final ClientConfigurationCommands clientConfigurationCommands;

    private ConfigRegistry(FitnessBotProperties fitnessBotProperties,
                           NotificationsProperties notificationsProperties,
                           WorkoutSizeProperties workoutSizeProperties,
                           ScheduledJobsProperties scheduledJobsProperties,
                           PathProperties pathProperties,
                           ClientConfigurationCommands clientConfigurationCommands) {
        this.fitnessBotProperties = fitnessBotProperties;
        this.notificationsProperties = notificationsProperties;
        this.workoutSizeProperties = workoutSizeProperties;
        this.scheduledJobsProperties = scheduledJobsProperties;
        this.pathProperties = pathProperties;
        this.clientConfigurationCommands = clientConfigurationCommands;
    }

    public static ConfigRegistry props() {
        if (configRegistry == null) {
            configRegistry = new ConfigRegistry(
                    initializeFitnessBotProperties(),
                    initializeNotificationProperties(),
                    initializeWorkoutSizeProperties(),
                    initializeScheduledJobsProperties(),
                    initializePathProperties(),
                    initializeClientConfigurationProperties());
        }
        return configRegistry;
    }

    private static PathProperties initializePathProperties() {
        return new PathProperties();
    }

    private static ScheduledJobsProperties initializeScheduledJobsProperties() {
        return toObjectProperty("scheduled_jobs", ScheduledJobsProperties.class);
    }

    private static NotificationsProperties initializeNotificationProperties() {
        return toObjectProperty("notifications", NotificationsProperties.class);
    }

    private static FitnessBotProperties initializeFitnessBotProperties() {
        FitnessBotProperties botProperties = toObjectProperty("fitness_bot", FitnessBotProperties.class);
        botProperties.setFitnessBotCredentials(new FitnessBotCredentials());
        return botProperties;
    }

    private static WorkoutSizeProperties initializeWorkoutSizeProperties() {
        return toObjectProperty("workout_size", WorkoutSizeProperties.class);
    }

    private static ClientConfigurationCommands initializeClientConfigurationProperties() {
        return toObjectProperty("client_configuration_commands", ClientConfigurationCommands.class);
    }

    private static <T> T toObjectProperty(String key, Class<T> clazz) {
        try {
            Yaml yaml = new Yaml();
            InputStream inputStream = new FileInputStream(PATH_TO_CONFIG);
            Map<String, Object> parsedConfig = yaml.load(inputStream);
            return ObjectMapperConfiguration.getObjectMapper().convertValue(parsedConfig.get(key), clazz);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(String.format("File does not exists under the following path: %s", PATH_TO_CONFIG), e);
        }
    }

    public FitnessBotProperties botProperties() {
        return fitnessBotProperties;
    }

    public NotificationsProperties notifications() {
        return notificationsProperties;
    }

    public ScheduledJobsProperties scheduledJobs() {
        return scheduledJobsProperties;
    }

    public WorkoutSizeProperties workoutSizes() {
        return workoutSizeProperties;
    }

    public PathProperties pathProperties() {
        return pathProperties;
    }

    public ClientConfigurationCommands clientConfigurationCommands() {
        return clientConfigurationCommands;
    }
}
