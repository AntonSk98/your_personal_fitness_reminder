package ansk.development.service.impl;

import ansk.development.configuration.ConfigRegistry;
import ansk.development.service.api.IFileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Implementation of {@link IFileManager}.
 *
 * @author Anton Skripin
 */
public class FileManager implements IFileManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileManager.class);

    private static FileManager fileManager;

    private FileManager() {
    }

    public static FileManager fileManager() {
        return Optional
                .ofNullable(fileManager)
                .orElseGet(() -> {
                    fileManager = new FileManager();
                    return fileManager;
                });
    }

    @Override
    public InputFile getGifFile(String directory, String name) {
        return this.getFile(directory, name, "gif");
    }

    @Override
    public InputFile getFile(String directory, String name, String extension) {
        final String path = String.format("%s/%s/%s.%s",
                ConfigRegistry.props().pathProperties().getPathToExercises(),
                directory,
                name,
                extension
        );
        try {
            InputStream file = new FileInputStream(path);
            return new InputFile(file, String.format("%s.%s", name, extension));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(String.format("No files are found in the following path: %s", path), e);
        }
    }

    @Override
    public long getFileCount(String directory) {
        final String path = String.format("%s%s",
                ConfigRegistry.props().pathProperties().getPathToExercises(),
                directory);
        try (Stream<Path> pathStream = Files.list(Paths.get(path))) {
            return pathStream.filter(Files::isRegularFile).count();
        } catch (IOException e) {
            LOGGER.error("Unable to get the number of files in directory {}", directory, e);
            return 0;
        }
    }
}
