package com.verapdf.restapi.entity;

import java.io.File;
import java.util.UUID;

public class Job {
    private UUID jobId;

    private File[] files;

    public Job() {
        setJobId(UUID.randomUUID());
    }

    public UUID getJobId() {
        return jobId;
    }

    private void setJobId(UUID jobId) {
        this.jobId = jobId;
    }

    public File[] getFiles() {
        return files;
    }

    public void setFiles(File[] files) {
        this.files = files;
    }
}
