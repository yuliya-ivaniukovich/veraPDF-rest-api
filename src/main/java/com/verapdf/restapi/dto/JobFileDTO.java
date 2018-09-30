package com.verapdf.restapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.verapdf.restapi.entity.FileType;

import java.io.File;
import java.nio.file.Paths;
import java.util.UUID;

public class JobFileDTO {
    @JsonIgnore
    private File file;

    private UUID jobId;
    private UUID fileId;
    private FileType type;
    private String path;
    //TODO: Cut pathes

    public JobFileDTO(File file, UUID jobId, UUID fileId, FileType type, String path) {
        this.setJobId(jobId);
        this.setFileId(fileId);
        this.setType(type);
        this.setPath(path);
        this.setFile(file);
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
