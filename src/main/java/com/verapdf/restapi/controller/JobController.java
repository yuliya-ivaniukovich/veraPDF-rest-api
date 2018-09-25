package com.verapdf.restapi.controller;

import com.verapdf.restapi.entity.Job;
import com.verapdf.restapi.service.interfaces.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public HttpStatus uploadFiles(@PathVariable UUID jobId, @RequestParam MultipartFile[] file) {
        Job job = jobService.getJob(jobId);
        if (job == null) {
            return HttpStatus.BAD_REQUEST;
        }
        jobService.setFiles(job, file);
        return HttpStatus.OK;
    }

    @DeleteMapping("/{jobId}/{fileNames}")
    public HttpStatus deleteFiles(@PathVariable UUID jobId, @PathVariable String[] fileNames) {
        Job job = jobService.getJob(jobId);
        if (job == null) {
            return HttpStatus.BAD_REQUEST;
        }
        jobService.deleteFiles(job, fileNames);
        return HttpStatus.OK;
    }

    @PostMapping("/{jobId}/{paths}")
    public String createPaths(@PathVariable UUID jobId, @PathVariable String[] paths) {
        return "create paths";
    }

    @DeleteMapping("/{jobId}/{paths}")
    public String deletePaths(@PathVariable UUID jobId, @PathVariable String[] paths) {
        return "deletePaths";
    }

}
