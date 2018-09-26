package com.verapdf.restapi.service;

import com.verapdf.restapi.entity.Job;
import com.verapdf.restapi.exception.FileNotFoundException;
import com.verapdf.restapi.exception.JobNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Service
public class JobService {
    private static final Logger log = LogManager.getLogger(JobService.class);

    private final static String JOB_NOT_FOUND = "Job not found.";
    private final static String FILE_NOT_FOUND = "File not found.";

    private Map<UUID, Job> jobHashMap;

    @Value("${jobs}")
    private String jobsPath;

    @Value("${pdfRoot}")
    private String pdfRootPath;

    public JobService() {
        jobHashMap = new HashMap<>();
    }

    public UUID createJob() {
        Job job = new Job(jobsPath, pdfRootPath);
        jobHashMap.put(job.getJobId(), job);
        return job.getJobId();
    }

    private Job getJob(UUID uuid) {
      return jobHashMap.get(uuid);
    }

    public void setFiles(UUID uuid, List<MultipartFile> files) {
        Job job = getJob(uuid);

        if (job == null) {
            throw new JobNotFoundException(JOB_NOT_FOUND);
        }

        job.addFiles(files);
    }

    public void deleteFiles(UUID uuid, List<String> fileNames) {
        Job job = getJob(uuid);
        if (job == null) {
            throw new JobNotFoundException(JOB_NOT_FOUND);
        }
        for (String fileName : fileNames) {
            Path filePath = Paths.get(jobsPath, job.getJobId().toString(), fileName);
            try {
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                log.error("Unable to delete file.", e);
            }
        }
    }

    public void setPath(UUID uuid, String path) {
        Job job = getJob(uuid);
        if (job == null) {
            throw new JobNotFoundException(JOB_NOT_FOUND);
        }
            if (Files.exists(Paths.get(path))){
                job.addFile(new File(path), false);
            }
            else {
                throw new FileNotFoundException(FILE_NOT_FOUND);
            }
    }

    public UUID closeJob(UUID uuid) {
        Job job = getJob(uuid);
        if (job == null) {
           return null;
        }
        job.close();

        return job.getJobId();
    }

}
