package org.flux.store.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;

public class FileBackup {

    private static Logger log = LoggerFactory.getLogger(FileBackup.class);

    public static String restoreBackup(String path) {
        String content = "";
        try {
            Path filePath = Paths.get(path);
            content = Files.readString(filePath);
        } catch (IOException e) {
            log.error("Could not read backup", e);
        }
        return content;
    }

    public static void saveBackup(String path, String content) {
        try {
            Path filePath = Paths.get(path);
            if(!Files.exists(filePath, LinkOption.NOFOLLOW_LINKS)) {
                Files.createFile(filePath);
            }
            Files.writeString(filePath, content, StandardOpenOption.CREATE);
        } catch (IOException e) {
            log.error("Could not save backup", e);
        }
    }
}
