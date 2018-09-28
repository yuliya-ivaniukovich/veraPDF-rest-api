package com.verapdf.restapi.entity;

import com.verapdf.restapi.dto.JobFileDTO;
import com.verapdf.restapi.exception.FileAlreadyExistsException;
import com.verapdf.restapi.exception.ResourceNotFoundException;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;

import java.io.*;

import org.apache.logging.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Job implements Closeable {

    private static final Logger log = LogManager.getLogger(Job.class);

    private final UUID jobId;

    private Map<UUID, File> files;

    private File jobDirectory;
    private File pdfDirectory;

    public Job(String rootDirectory, String pdfDirectory) {
        files = new HashMap<>();
        this.jobId = prepareJob(rootDirectory, pdfDirectory);
    }

    public UUID getJobId() {
        return jobId;
    }

    private UUID prepareJob(String rootDirectory, String pdfDirectory) {
        //TODO: check

        UUID uuid = UUID.randomUUID();
        File file = new File(this.pdfDirectory, uuid.toString());
        while (file.exists()) {
            uuid = UUID.randomUUID();
            file = new File(this.pdfDirectory, uuid.toString());
        }

        this.jobDirectory = new File(rootDirectory, uuid.toString());
        this.pdfDirectory = new File(this.jobDirectory, pdfDirectory);
        this.pdfDirectory.mkdirs();

        return uuid;
    }

    public JobFileDTO addSingleFile(MultipartFile file) {
        //TODO: move to service ?
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new ResourceNotFoundException();
        }

        File newFile = new File(pdfDirectory, originalFilename);

        if (newFile.exists()) {
            throw new FileAlreadyExistsException();
        }

        try  {
            FileUtils.copyInputStreamToFile(file.getInputStream(), newFile);
        } catch (IOException e) {
            log.error("Unable to transfer file.", e);
        }
        UUID newFileUUID = UUID.randomUUID();
        files.put(newFileUUID, newFile);
        return new JobFileDTO(newFile, this.jobId, newFileUUID, FileType.REMOTE, newFile.getAbsolutePath());
    }

    public JobFileDTO addSinglePath(String path) {
        File file = new File(path);
        for (File item : files.values()) {
            if (file.equals(item)) {
             throw new FileAlreadyExistsException();
            }
        }
        UUID newUUID = UUID.randomUUID();
        files.put(newUUID, file);
        return new JobFileDTO(file, this.jobId, newUUID, FileType.LOCAL, file.getAbsolutePath());
    }

    public void deleteFile(UUID uuid) {
        //todo: check
        File file = files.get(uuid);
        if (file.exists() && pdfDirectory.equals(file.getParentFile())) {
            file.delete();
        }
        else {
            throw new ResourceNotFoundException();
        }
    }

    @Override
    public void close() {
        try {
            for (Map.Entry<UUID, File> entry : files.entrySet()) {
                Files.deleteIfExists(Paths.get(entry.getValue().getAbsolutePath()));
            }
        } catch (IOException e) {
            log.error("Unable to delete the file", e);
        }

        try {
            org.apache.commons.io.FileUtils.deleteDirectory(jobDirectory);
        } catch (IOException e) {
            log.error("Unable to delete directory", e);
        }

    }
}
