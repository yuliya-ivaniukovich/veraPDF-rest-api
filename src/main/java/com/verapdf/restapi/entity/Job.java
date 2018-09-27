package com.verapdf.restapi.entity;

import com.verapdf.restapi.utils.FileUtils;
import org.apache.logging.log4j.LogManager;

import java.io.*;

import org.apache.logging.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class Job implements Closeable {

    private static final Logger log = LogManager.getLogger(Job.class);

    private UUID jobId;

    private List<File> files;
    private List<File> tempFiles;

    private Path jobDirectory;
    private Path pdfDirectory;

    public Job(String rootDirectory, String pdfDirectory) {
        files = new LinkedList<>();
        tempFiles = new LinkedList<>();
        this.jobId = UUID.randomUUID();
        this.jobDirectory = Paths.get(rootDirectory, jobId.toString());
        this.pdfDirectory = Paths.get(rootDirectory, jobId.toString(), pdfDirectory);
        prepareJob();
    }

    private void prepareJob() {
        FileUtils.createDirectory(pdfDirectory);
    }

    public UUID getJobId() {
        return jobId;
    }

    public void addFiles(List<MultipartFile> files) {
        for (MultipartFile file : files) {
            addSingleFile(file);
        }
    }

    public void addSingleFile(MultipartFile file) {
        Path filePath = Paths.get(pdfDirectory.toString(), file.getOriginalFilename());
        File newFile = new File(filePath.toString());
        try (
                InputStream inputStream =file.getInputStream();
                OutputStream outputStream = new FileOutputStream(newFile)
        ) {
            int read;
            byte[] bytes = new byte[1024];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
            tempFiles.add(newFile);
        } catch (IOException e) {
            log.error("Unable to transfer file.", e);
        }

    }

    public void addSinglePath(String path) {
        File file = new File(path);
        files.add(file);
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
        } catch (IOException e) {
            log.error("Unable to delete the file", e);
        }

        try {
            org.apache.commons.io.FileUtils.deleteDirectory(new File(jobDirectory.toString()));
        } catch (IOException e) {
            log.error("Unable to delete directory", e);
        }

    }
}
