package com.verapdf.restapi.controller;

import com.verapdf.restapi.entity.Job;
import com.verapdf.restapi.service.interfaces.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/job")
public class JobController {

    private JobService jobService;

    @Autowired
    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping(produces = {"application/JSON"})
    public Map startJob() {
        Job job = new Job();
        jobService.addJob(job);
        return Collections.singletonMap("id", job.getJobId().toString());
    }

    @PostMapping("/{jobId}/files")
    public String uploadFiles(@PathVariable UUID jobId, @RequestParam("files")MultipartFile[] file) {
        Job job = jobService.getJob(jobId);
        return "upload files";
    }

    @DeleteMapping("/{jobId}/files}")
    public String deleteFiles(@PathVariable UUID jobId) {
        Job job = jobService.getJob(jobId);
        return "delete files";
    }

    @PostMapping("/{jobId}/paths")
    public String createPaths(@PathVariable UUID jobId) {
        return "create paths";
    }

    @DeleteMapping("/{jobId}/paths")
    public String deletePaths(@PathVariable UUID jobId) {
        return "deletePaths";
    }

}
