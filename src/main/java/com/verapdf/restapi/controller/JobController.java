package com.verapdf.restapi.controller;

import com.verapdf.restapi.dto.JobDTO;
import com.verapdf.restapi.dto.JobFileDTO;
import com.verapdf.restapi.dto.PathDTO;
import com.verapdf.restapi.executor.Job;
import com.verapdf.restapi.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.File;
import java.net.URI;
import java.util.*;

@RestController
@RequestMapping("/jobs")
public class JobController {

    private JobService jobService;

    @Autowired
    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> startJob() {
        UUID uuid = jobService.createJob();
        URI location = MvcUriComponentsBuilder
                .fromMethodName(JobController.class, "getJob", uuid)
                .build()
                .toUri();
        return ResponseEntity.created(location).body(Collections.singletonMap("jobId", uuid.toString()));
    }

    @GetMapping(value = "/{jobId}")
    public ResponseEntity<JobDTO> getJob(@PathVariable UUID jobId) {
        Job job = jobService.getJob(jobId);
        JobDTO jobDTO = new JobDTO(job.getJobId());
        return ResponseEntity.ok(jobDTO);
    }

    @DeleteMapping(value = "/{jobId}")
    public ResponseEntity<String> closeJob(@PathVariable UUID jobId) {
        jobService.closeJob(jobId);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value="/{jobId}/files", headers = "content-type=multipart/form-data")
    public ResponseEntity<JobFileDTO> uploadFiles(@PathVariable UUID jobId, @RequestParam("file") MultipartFile file) {
        JobFileDTO jobDTO = jobService.addFile(jobId, file);
        URI location = generateFileURI(jobDTO.getJobId(), jobDTO.getFileId());
        return ResponseEntity.created(location).body(jobDTO);
    }

    @PostMapping(value = "/{jobId}/files", headers = "content-type=application/json")
    public ResponseEntity<JobFileDTO> createPaths(@PathVariable UUID jobId, @RequestBody PathDTO dto) {
        JobFileDTO jobDTO = jobService.addPath(jobId, dto.getPath());
        URI location = generateFileURI(jobDTO.getJobId(), jobDTO.getFileId());
        return ResponseEntity.created(location).body(jobDTO);
    }

    @GetMapping(value = "/{jobId}/files/{fileId}", headers = "content-type=multipart/form-data")
    public ResponseEntity<FileSystemResource> getFile(@PathVariable UUID jobId, @PathVariable UUID fileId) {
        File file = jobService.getJobFile(jobId, fileId);
        return ResponseEntity.ok().body(new FileSystemResource(file));
    }

    @GetMapping(value = "/{jobId}/files/{fileId}", headers = "content-type=application/json")
    public ResponseEntity<PathDTO> getPath(@PathVariable UUID jobId, @PathVariable UUID fileId) {
        PathDTO pathDTO = jobService.getJobFilePath(jobId, fileId);
        return ResponseEntity.ok().body(pathDTO);
    }

    @DeleteMapping("/{jobId}/files/{fileId}")
    public ResponseEntity<JobFileDTO> deleteFile(@PathVariable UUID jobId, @PathVariable UUID fileId) {
        jobService.deleteFile(jobId, fileId);
        return ResponseEntity.ok().build();
    }

    private URI generateFileURI(UUID jobId, UUID fileId) {
        return MvcUriComponentsBuilder
                .fromMethodName(JobController.class, "deleteFile", jobId, fileId)
                .build().toUri();
    }

}
