package com.verapdf.restapi.service;

import com.verapdf.restapi.dto.JobFileDTO;
import com.verapdf.restapi.entity.Job;
import com.verapdf.restapi.exception.ResourceNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


//TODO: Clear feature/policy (4/Error)
@Service
public class JobService {
    private static final Logger log = LogManager.getLogger(JobService.class);

    private final static String JOB_NOT_FOUND = "Job not found.";
    private final static String FILE_NOT_FOUND = "File not found.";

    private Map<UUID, Job> jobHashMap;

    @Value("${jobsBaseDir}")
    private String jobsBaseDir;

    @Value("${pdfDir}")
    private String pdfDir;

    public JobService() {
        jobHashMap = new HashMap<>();
    }

    public UUID createJob() {
        Job job = new Job(jobsBaseDir, pdfDir);
        UUID jobId = job.getJobId();
        jobHashMap.put(jobId, job);
        return jobId;
    }

    private Job getJob(UUID uuid) {
      return jobHashMap.get(uuid);
    }

    public JobFileDTO addFile(UUID uuid, MultipartFile file) {
        Job job = getJob(uuid);
        if (job == null) {
            throw new ResourceNotFoundException(JOB_NOT_FOUND);
        }
        return job.addSingleFile(file);
    }

    public void deleteFile(UUID jobUUID, UUID fileUUID) {
        Job job = getJob(jobUUID);
        if (job == null) {
            throw new ResourceNotFoundException(JOB_NOT_FOUND);
        }
        job.deleteFile(fileUUID);
    }

    public JobFileDTO addPath(UUID uuid, String path) {
        Job job = getJob(uuid);
        if (job == null) {
            throw new ResourceNotFoundException(JOB_NOT_FOUND);
        }
            if (Files.exists(Paths.get(path))){
                return job.addSinglePath(path);
            }
            else {
                throw new ResourceNotFoundException(FILE_NOT_FOUND);
            }
    }

    public void closeJob(UUID uuid) {
        Job job = getJob(uuid);
        if (job == null) {
           throw new ResourceNotFoundException(JOB_NOT_FOUND);
        }
        jobHashMap.remove(uuid);
        job.close();
    }

}
