package com.verapdf.restapi.dto;

import java.util.UUID;

public class JobDTO {
    private UUID jobId;

    public JobDTO() { }

    public JobDTO(UUID jobId) {
        this.setJobId(jobId);
    }

    public UUID getJobId() {
        return jobId;
    }

    public void setJobId(UUID jobId) {
        this.jobId = jobId;
    }
}
