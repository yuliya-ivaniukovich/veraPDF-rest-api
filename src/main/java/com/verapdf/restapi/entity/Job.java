package com.verapdf.restapi.entity;

import java.io.File;
import java.util.LinkedList;
import java.util.UUID;

public class Job {
    private UUID jobId;

    private LinkedList<File> files;

    public Job() {
        files = new LinkedList<>();
        setJobId(UUID.randomUUID());
    }

    public UUID getJobId() {
        return jobId;
    }

    private void setJobId(UUID jobId) {
        this.jobId = jobId;
    }

    public LinkedList<File> getFiles() {
        return files;
    }

    public void addFile(File file) {
        files.add(file);
    }
 }
