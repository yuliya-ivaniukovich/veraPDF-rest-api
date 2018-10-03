package com.verapdf.restapi.executor;

import com.verapdf.restapi.dto.JobFileDTO;
import com.verapdf.restapi.exception.BadRequestException;
import com.verapdf.restapi.exception.ResourceNotFoundException;
import com.verapdf.restapi.exception.VeraPDFRestApiException;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class Job implements Closeable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Job.class);
    private static final int MAX_CHECK_UUID_ITERATIONS = 10;

    private final String FILE_ALREADY_EXISTS = "File already exists";
    private final String FILE_NOT_FOUND = "File not found";
    private final String UNABLE_TO_DELETE_FILE = "Unable to delete file";
    private final String UNABLE_TO_TRANSFER_FILE = "Unable to transfer file";

    private final UUID jobId;

    private Map<UUID, File> files;

    private File jobDirectory;
    private File pdfDirectory;

    public Job(String rootDirectory, String pdfDirectory) {
        files = new HashMap<>();
        this.jobId = prepareJob(rootDirectory, pdfDirectory);
    }

    private UUID prepareJob(String rootDirectory, String pdfDirectory) {
        UUID uuid;

        do {
            uuid = UUID.randomUUID();
            this.jobDirectory = new File(rootDirectory, uuid.toString());
        } while (jobDirectory.exists());

        this.pdfDirectory = new File(this.jobDirectory, pdfDirectory);
        try {
            FileUtils.forceMkdir(this.jobDirectory);
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

        try {
            FileUtils.copyInputStreamToFile(file.getInputStream(), newFile);
        } catch (IOException e) {
            LOGGER.error("Unable to transfer file", e);
            throw new BadRequestException(UNABLE_TO_TRANSFER_FILE);
        }
        UUID newFileUUID = getUniqueFileUUID();
        files.put(newFileUUID, newFile);
        String path = getRelativeFilePath(newFile);
        return new JobFileDTO(this.jobId, newFileUUID, newFile, path, JobFileDTO.FileType.REMOTE);
    }

    public JobFileDTO addSinglePath(String path) {
        File file = new File(path);
        UUID newUUID;

        if (!file.exists()) {
            throw new ResourceNotFoundException(FILE_NOT_FOUND);
        }

        for (File item : files.values()) {
            if (file.equals(item)) {
                throw new BadRequestException(FILE_ALREADY_EXISTS);
            }
        }
        newUUID = getUniqueFileUUID();
        files.put(newUUID, file);

        return new JobFileDTO(this.jobId, newUUID, file, file.getAbsolutePath(), JobFileDTO.FileType.LOCAL);
    }

    public JobFileDTO deleteFile(UUID fileId) {
        if (!files.containsKey(fileId)) {
            throw new ResourceNotFoundException(FILE_NOT_FOUND);
        }
        File file = files.get(fileId);
        String filePath = file.getAbsolutePath();
        JobFileDTO.FileType fileType = getFileType(file);

        if (fileType == JobFileDTO.FileType.REMOTE) {
            if (!file.delete()) {
                LOGGER.warn(UNABLE_TO_DELETE_FILE);
            }
        }

        files.remove(fileId);
        return new JobFileDTO(this.jobId, fileId, file, filePath, fileType);
    }

    public JobFileDTO getJobFile(UUID fileId) {
        File file = getFile(fileId);
        JobFileDTO.FileType fileType = getFileType(file);
        String path;
        if (fileType == JobFileDTO.FileType.REMOTE) {
            path = getRelativeFilePath(file);
        } else {
            path = file.getAbsolutePath();
        }
        return new JobFileDTO(this.jobId, fileId, file, path, fileType);
    }

    public UUID getJobId() {
        return jobId;
    }

    @Override
    public void close() {
        try {
            org.apache.commons.io.FileUtils.deleteDirectory(jobDirectory);
        } catch (IOException e) {
            LOGGER.error("Unable to delete directory", e);
        }

    }

    private UUID getUniqueFileUUID() {
        for (int flag = 0; flag < MAX_CHECK_UUID_ITERATIONS; ++flag) {
            UUID uuid = UUID.randomUUID();
            if (!files.containsKey(uuid)) {
                return uuid;
            }
        }
        throw new VeraPDFRestApiException();
    }

    private String getRelativeFilePath(File file) {
        return jobDirectory.toURI().relativize(file.toURI()).getPath();
    }

    private JobFileDTO.FileType getFileType(File file) {
        JobFileDTO.FileType fileType;

        if (file.exists() && pdfDirectory.equals(file.getParentFile())) {
            fileType = JobFileDTO.FileType.REMOTE;
        } else {
            fileType = JobFileDTO.FileType.LOCAL;
        }
        return fileType;
    }

    private File getFile(UUID fileId) {
        if (!files.containsKey(fileId)) {
            throw new ResourceNotFoundException(FILE_NOT_FOUND);
        }
        return files.get(fileId);
    }
}
