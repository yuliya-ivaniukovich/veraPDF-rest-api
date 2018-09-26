package com.verapdf.restapi.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class FileUtils {

    private static final Logger log = LogManager.getLogger(FileUtils.class);

    public static void createDirectory(Path path) {

        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            log.error("Error creating directory", e);
        }
    }
}
