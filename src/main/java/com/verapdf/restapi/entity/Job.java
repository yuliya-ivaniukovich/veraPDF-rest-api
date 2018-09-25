package com.verapdf.restapi.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class Job implements Closeable {

    private static final Logger log = LoggerFactory.getLogger(Job.class);

    private UUID jobId;

    private List<File> files;
    private List<File> tempFiles;

    public Job() {
        files = new LinkedList<>();
        tempFiles = new LinkedList<>();
        setJobId(UUID.randomUUID());
    }

    public UUID getJobId() {
        return jobId;
    }

    private void setJobId(UUID jobId) {
        this.jobId = jobId;
    }

    public void addFile(File file, boolean isTemp) {
        if (isTemp) {
            tempFiles.add(file);
        }
        else {
            files.add(file);
        }
    }

    public void deleteFiles(String[] paths) {
        for (String path : paths) {
            files.removeIf(file -> file.getPath().equals(path));
        }
    }

    @Override
    public void close() {
        try {
            for (File file : tempFiles) {
                Files.deleteIfExists(Paths.get(file.getAbsolutePath()));
            }
        }
        catch (IOException e) {
            log.error("Unable to delete the file");
        }

    }
}
