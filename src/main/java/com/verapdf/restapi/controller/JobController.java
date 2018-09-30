package com.verapdf.restapi.controller;

import com.verapdf.restapi.dto.JobFileDTO;
import com.verapdf.restapi.dto.PathDTO;
import com.verapdf.restapi.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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
    public ResponseEntity<Map<String, String>> startJob() {
        UUID uuid = jobService.createJob();
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{jobId}")
                .buildAndExpand(uuid)
                .toUri();
        return ResponseEntity.created(location).body(Collections.singletonMap("jobId", uuid.toString()));
    }

    @DeleteMapping(value = "/{jobId}")
    public ResponseEntity<String> closeJob(@PathVariable UUID jobId) {
        jobService.closeJob(jobId);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value="/{jobId}/files", headers = "content-type=multipart/form-data")
    public ResponseEntity<JobFileDTO> uploadFiles(@PathVariable UUID jobId, @RequestParam("file") MultipartFile file) {

        JobFileDTO dto = jobService.addFile(jobId, file);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{fileId}")
                .build()
                .expand(dto.getFileId())
                .toUri();

        return ResponseEntity.created(location).body(dto);
    }
    @PostMapping(value = "/{jobId}/files", headers = "content-type=application/json")
    public ResponseEntity<JobFileDTO> createPaths(@PathVariable UUID jobId, @RequestBody PathDTO dto) {
        JobFileDTO jobDTO = jobService.addPath(jobId, dto.getPath());
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{fileId}")
                .buildAndExpand(jobDTO.getFileId())
                .toUri();
        return ResponseEntity.created(location).body(jobDTO);
    }

    @DeleteMapping("/{jobId}/files/{fileId}")
    public ResponseEntity<JobFileDTO> deleteFiles(@PathVariable UUID jobId, @PathVariable UUID fileId) {

        jobService.deleteFile(jobId, fileId);

        return ResponseEntity.ok().build();
    }

}
