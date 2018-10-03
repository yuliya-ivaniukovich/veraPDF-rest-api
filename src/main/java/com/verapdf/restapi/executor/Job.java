package com.verapdf.restapi.executor;

import com.verapdf.restapi.dto.JobFileDTO;
import com.verapdf.restapi.exception.ResourceNotFoundException;
import com.verapdf.restapi.exception.BadRequestException;
import com.verapdf.restapi.exception.VeraPDFRestApiException;
import org.apache.commons.io.FileUtils;

import java.io.*;

import org.omg.CORBA.PUBLIC_MEMBER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

//TODO: uuid overflow
//TODO: init
public class Job implements Closeable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Job.class);
    private static final int MAX_CHECK_UUID_ITERATIONS = 10;

    private final String FILE_ALREADY_EXISTS = "FILE ALREADY EXISTS";
    private final String FILE_NOT_FOUND = "FILE NOT FOUND";
    private final String UNABLE_TO_DELETE_FILE = "UNABLE TO DELETE FILE";

    private final UUID jobId;

    private Map<UUID, File> files;

    private File jobDirectory;
    private File pdfDirectory;

    public Job(String rootDirectory, String pdfDirectory) {
        files = new HashMap<>();
        this.jobId = prepareJob(rootDirectory, pdfDirectory);
    }

    @PostConstruct
    private void init () {

    }

    private UUID prepareJob(String rootDirectory, String pdfDirectory) {
        UUID uuid;

        do {
             uuid = UUID.randomUUID();
            this.jobDirectory = new File(rootDirectory, uuid.toString());
        } while(jobDirectory.exists());

        this.pdfDirectory = new File(this.jobDirectory, pdfDirectory);
        try {
            FileUtils.forceMkdir(this.pdfDirectory);
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }

        return uuid;
    }

    public JobFileDTO addSingleFile(MultipartFile file) {

        String originalFilename = file.getOriginalFilename();

        if (originalFilename == null) {
            throw new ResourceNotFoundException(FILE_NOT_FOUND);
        }

        File newFile = new File(pdfDirectory, originalFilename);

        if (newFile.exists()) {
            throw new BadRequestException(FILE_ALREADY_EXISTS);
        }

        try  {
            FileUtils.copyInputStreamToFile(file.getInputStream(), newFile);
        } catch (IOException e) {
            LOGGER.error("Unable to transfer file.", e);
        }
        UUID newFileUUID = getUniqueUUID();
        files.put(newFileUUID, newFile);
        String path = jobDirectory.toURI().relativize(newFile.toURI()).getPath();
        return new JobFileDTO(this.jobId, newFileUUID, newFile, path, JobFileDTO.FileType.REMOTE);
    }

    public JobFileDTO addSinglePath(String path) {
        File file = new File(path);
        UUID newUUID;

        if (Files.exists(Paths.get(path))){
            for (File item : files.values()) {
                if (file.equals(item)) {
                    throw new BadRequestException(FILE_ALREADY_EXISTS);
                }
            }
            newUUID = getUniqueUUID();
            files.put(newUUID, file);
        }
        else {
            throw new ResourceNotFoundException(FILE_NOT_FOUND);
        }

        return new JobFileDTO(this.jobId, newUUID, file, file.getAbsolutePath(), JobFileDTO.FileType.LOCAL);
    }

    public void deleteFile(UUID uuid) {

        File file = files.get(uuid);
        if (file == null) {
            throw new ResourceNotFoundException(FILE_NOT_FOUND);
        }
        if (file.exists() && pdfDirectory.equals(file.getParentFile())) {
            if(!file.delete()) {
                LOGGER.warn(UNABLE_TO_DELETE_FILE);
            }
        }
        files.remove(uuid);
    }

    @Override
    public void close() {

        try {
            org.apache.commons.io.FileUtils.deleteDirectory(jobDirectory);
        } catch (IOException e) {
            LOGGER.error("Unable to delete directory", e);
        }

    }

    private UUID getUniqueUUID() {

        UUID uuid;
        int flag = 0;
        do {
            if (flag > MAX_CHECK_UUID_ITERATIONS) {
                throw new VeraPDFRestApiException();
            }
            uuid = UUID.randomUUID();
            ++flag;
        } while (files.containsKey(uuid));

        return uuid;
    }

    public String getFilePath(UUID fileId) {
        if (!files.containsKey(fileId)) {
            throw new ResourceNotFoundException(FILE_NOT_FOUND);
        }
        return files.get(fileId).getAbsolutePath();
    }

    public File getFile(UUID fileId) {
        if (!files.containsKey(fileId)) {
            throw new ResourceNotFoundException(FILE_NOT_FOUND);
        }
        return files.get(fileId);
    }


    public UUID getJobId() {
        return jobId;
    }
}
