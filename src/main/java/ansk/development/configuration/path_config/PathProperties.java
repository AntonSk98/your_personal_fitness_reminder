package ansk.development.configuration.path_config;

/**
 * Contains paths to technical configuration and exercise directories taken from environment variables.
 *
 * @author Anton Skripin
 */
public class PathProperties {

    private static final String EXERCISES_PATH_ENV_VARIABLE_NAME = "PATH_TO_EXERCISES";
    private static final String PROPERTIES_PATH_ENV_VARIABLE_NAME = "PATH_TO_CONFIG";

    private final String pathToConfig;
    private final String pathToExercises;


    public PathProperties() {
        this.pathToConfig = System.getenv(PROPERTIES_PATH_ENV_VARIABLE_NAME);
        this.pathToExercises = System.getenv(EXERCISES_PATH_ENV_VARIABLE_NAME);
        assert pathToConfig != null;
        assert pathToExercises != null;
    }

    public String getPathToConfig() {
        return pathToConfig;
    }

    public String getPathToExercises() {
        return pathToExercises;
    }
}
