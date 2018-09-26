package com.verapdf.restapi.controller;

import com.verapdf.restapi.dto.PathDTO;
import com.verapdf.restapi.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/jobs")
public class JobController {

    private final static String JOB_NOT_FOUND = "Job not found.";

    private JobService jobService;

    @Autowired
    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping
    public Map<String, String> startJob() {
        UUID uuid = jobService.createJob();
        return Collections.singletonMap("jobId", uuid.toString());
    }

    @DeleteMapping(value = "/{jobId}")
    public ResponseEntity<String> closeJob(@PathVariable UUID jobId) {
        UUID uuid = jobService.closeJob(jobId);
        if (uuid == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(JOB_NOT_FOUND);
        }
        return ResponseEntity.ok(uuid.toString());
    }

    @PostMapping(value="/{jobId}/files")
    public ResponseEntity<String> uploadFiles(@PathVariable UUID jobId, @RequestParam("file") List<MultipartFile> files) {
        jobService.setFiles(jobId, files);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{jobId}/localFiles")
    public ResponseEntity<String> deleteFiles(@PathVariable UUID jobId, @RequestParam("fileName") List<String> fileNames) {
        jobService.deleteFiles(jobId, fileNames);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{jobId}/localFiles")
    public ResponseEntity<String> createPaths(@PathVariable UUID jobId, @RequestBody PathDTO dto) {
         jobService.setPath(jobId, dto.getLocalPath());

        return ResponseEntity.ok().build();
    }

}
