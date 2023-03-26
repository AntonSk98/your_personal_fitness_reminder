package ansk.development.utils;

import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.*;

/**
 * Util method to handle file-related operations.
 */
public class FileUtils {

    private FileUtils() {

    }

    public static InputFile getGifFile(String directory, String name) {
        return FileUtils.getFile(directory, name, "gif");
    }

    public static InputFile getFile(String directory, String name, String extension) {
        InputStream file = FileUtils.class.getResourceAsStream(String.format("/%s/%s.%s", directory, name, extension));
        return new InputFile(file, String.format("%s.%s", name, extension));
    }
}
