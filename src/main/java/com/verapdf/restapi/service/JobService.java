package com.verapdf.restapi.service;

import com.verapdf.restapi.dto.JobFileDTO;
import com.verapdf.restapi.dto.PathDTO;
import com.verapdf.restapi.exception.ResourceNotFoundException;
import com.verapdf.restapi.executor.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

//todo: clear maven
@Service
public class JobService {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobService.class);

    private final static String JOB_NOT_FOUND = "Job not found.";

    private Map<UUID, Job> jobMap;

    @Value("${jobsBaseDir}")
    private String jobsBaseDir;

    @Value("${pdfDir}")
    private String pdfRelativeDir;

    public JobService() {
        jobMap = new HashMap<>();
    }

    public UUID createJob() {
        Job job = new Job(jobsBaseDir, pdfRelativeDir);
        UUID jobId = job.getJobId();
        jobMap.put(jobId, job);
        return jobId;
    }

    public Job getJob(UUID uuid) {
        if (!jobMap.containsKey(uuid)) {
            throw new ResourceNotFoundException(JOB_NOT_FOUND);
        }
        return jobMap.get(uuid);
    }

    public JobFileDTO addFile(UUID uuid, MultipartFile file) {
        Job job = getJob(uuid);

        return job.addSingleFile(file);
    }

    public void deleteFile(UUID jobUUID, UUID fileUUID) {
        Job job = getJob(jobUUID);

        job.deleteFile(fileUUID);
    }

    public JobFileDTO addPath(UUID uuid, String path) {
        Job job = getJob(uuid);
        return job.addSinglePath(path);
    }

    public void closeJob(UUID uuid) {
        Job job = getJob(uuid);

        job.close();
        jobMap.remove(uuid);
    }

    public PathDTO getJobFilePath(UUID jobId, UUID fileId) {
        Job job = getJob(jobId);
        return new PathDTO(JobFileDTO.FileType.LOCAL, job.getFilePath(fileId));
    }

    public File getJobFile(UUID jobId, UUID fileId) {
        Job job = getJob(jobId);
        return job.getFile(fileId);
    }

}
