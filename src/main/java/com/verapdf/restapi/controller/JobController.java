package com.verapdf.restapi.controller;

import com.verapdf.restapi.entity.Job;
import com.verapdf.restapi.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/job")
public class JobController {

    private final static String JOB_NOT_FOUND = "Job not found.";

    private JobService jobService;

    @Autowired
    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping
    public Map<String, String> startJob() {
        Job job = new Job();
        jobService.addJob(job);
        return Collections.singletonMap("jobId", job.getJobId().toString());
    }

    @DeleteMapping(value = "/{jobId}")
    public ResponseEntity<String> closeJob(@PathVariable UUID jobId) {
        Job job = jobService.getJob(jobId);
        if (job == null) {
            return ResponseEntity.badRequest().body(JOB_NOT_FOUND);
        }
        jobService.closeJob(job);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value="/{jobId}/files")
    public ResponseEntity<String> uploadFiles(@PathVariable UUID jobId, @RequestParam("file") MultipartFile[] files) {
        Job job = jobService.getJob(jobId);

        if (job == null) {
            return ResponseEntity.badRequest().body(JOB_NOT_FOUND);
        }

        jobService.setFiles(job, files);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{jobId}/localFiles")
    public ResponseEntity<String> deleteFiles(@PathVariable UUID jobId, @RequestParam("fileName") String[] fileNames) {
        Job job = jobService.getJob(jobId);
        if (job == null) {
            return ResponseEntity.badRequest().body(JOB_NOT_FOUND);
        }
        jobService.deleteFiles(job, fileNames);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{jobId}/localPaths")
    public ResponseEntity<String> createPaths(@PathVariable UUID jobId, @RequestParam("path") String[] paths) {

        Job job = jobService.getJob(jobId);

        if (job == null) {
            return ResponseEntity.badRequest().body(JOB_NOT_FOUND);
        }

        jobService.setPaths(job, paths);

        return ResponseEntity.ok().build();
    }

}
