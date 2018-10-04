package com.verapdf.restapi.service;

import com.verapdf.restapi.dto.JobDTO;
import com.verapdf.restapi.dto.JobFileDTO;
import com.verapdf.restapi.exception.ResourceNotFoundException;
import com.verapdf.restapi.executor.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class JobService {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobService.class);

    private final static String JOB_NOT_FOUND = "Job not found";

    private Map<UUID, Job> jobMap;

    @Value("${jobsBaseDir}")
    private String jobsBaseDir;

    @Value("${pdfDir}")
    private String pdfRelativeDir;

    public JobService() {
        jobMap = new HashMap<>();
    }

    public JobDTO createJob() {
        Job job = new Job(jobsBaseDir, pdfRelativeDir);
        jobMap.put(job.getJobId(), job);
        return new JobDTO(job.getJobId());
    }

    public JobFileDTO addFile(UUID uuid, MultipartFile file) {
        Job job = getJob(uuid);

        return job.addSingleFile(file);
    }

    public JobFileDTO deleteFile(UUID jobUUID, UUID fileUUID) {
        Job job = getJob(jobUUID);

        return job.deleteFile(fileUUID);
    }

    public JobFileDTO addPath(UUID uuid, String path) {
        Job job = getJob(uuid);
        return job.addSinglePath(path);
    }

    public JobDTO closeJob(UUID uuid) {
        Job job = getJob(uuid);

        job.close();
        jobMap.remove(uuid);
        return new JobDTO(uuid);
    }

    public JobFileDTO getJobFile(UUID jobId, UUID fileId) {
        Job job = getJob(jobId);
        return job.getJobFile(fileId);
    }

    public JobDTO getJobDTO(UUID jobId) {
        if (!jobMap.containsKey(jobId)) {
            throw new ResourceNotFoundException(JOB_NOT_FOUND);
        }
        return new JobDTO(jobId);
    }

    private Job getJob(UUID uuid) {
        if (!jobMap.containsKey(uuid)) {
            throw new ResourceNotFoundException(JOB_NOT_FOUND);
        }
        return jobMap.get(uuid);
    }
}
