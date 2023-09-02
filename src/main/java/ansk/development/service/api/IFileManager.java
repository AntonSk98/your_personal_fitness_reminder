package ansk.development.service.api;

import org.telegram.telegrambots.meta.api.objects.InputFile;

/**
 * Contains methods to manage and handle files.
 *
 * @author Anton Skripin
 */
public interface IFileManager {

    InputFile getGifFile(String directory, String name);

    InputFile getFile(String directory, String name, String extension);

    long getFileCount(String directory);
}
