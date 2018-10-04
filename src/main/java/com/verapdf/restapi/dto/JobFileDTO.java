package com.verapdf.restapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.File;
import java.util.UUID;

public class JobFileDTO {

    public enum FileType {
        REMOTE,
        LOCAL
    }

    @JsonIgnore
    private File file;

    private UUID jobId;
    private UUID fileId;
    private FileType type;
    private String path;

    public JobFileDTO() {
    }

    public JobFileDTO(UUID jobId, UUID fileId, File file, String path, FileType type) {
        this.setJobId(jobId);
        this.setFileId(fileId);
        this.setFile(file);
        this.setPath(path);
        this.setType(type);
    }

    public UUID getJobId() {
        return jobId;
    }

    public void setJobId(UUID jobId) {
        this.jobId = jobId;
    }

    public UUID getFileId() {
        return fileId;
    }

    public void setFileId(UUID fileId) {
        this.fileId = fileId;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public FileType getType() {
        return type;
    }

    public void setType(FileType type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
