package com.verapdf.restapi.controller;

import com.verapdf.restapi.dto.JobDTO;
import com.verapdf.restapi.dto.JobFileDTO;
import com.verapdf.restapi.dto.PathDTO;
import com.verapdf.restapi.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

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
        return ResponseEntity.ok(jobService.getJobDTO(jobId));
    }

    @DeleteMapping(value = "/{jobId}")
    public ResponseEntity<String> closeJob(@PathVariable UUID jobId) {
        jobService.closeJob(jobId);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/{jobId}/files", headers = "content-type=multipart/form-data")
    public ResponseEntity<JobFileDTO> uploadFiles(@PathVariable UUID jobId, @RequestParam MultipartFile file) {
        JobFileDTO jobFileDTO = jobService.addFile(jobId, file);
        URI location = generateFileURI(jobFileDTO.getJobId(), jobFileDTO.getFileId());
        return ResponseEntity.created(location).body(jobFileDTO);
    }

    @PostMapping(value = "/{jobId}/files", headers = "content-type=application/json")
    public ResponseEntity<JobFileDTO> createPaths(@PathVariable UUID jobId, @RequestBody PathDTO pathDTO) {
        JobFileDTO jobFileDTO = jobService.addPath(jobId, pathDTO.getPath());
        URI location = generateFileURI(jobFileDTO.getJobId(), jobFileDTO.getFileId());
        return ResponseEntity.created(location).body(jobFileDTO);
    }


    @GetMapping(value = "/{jobId}/files/{fileId}")
    public ResponseEntity<JobFileDTO> getJobFileDTO(@PathVariable UUID jobId, @PathVariable UUID fileId) {
        JobFileDTO jobFileDTO = jobService.getJobFileDTO(jobId, fileId);
        return ResponseEntity.ok(jobFileDTO);
    }

    @DeleteMapping("/{jobId}/files/{fileId}")
    public ResponseEntity<JobFileDTO> deleteFile(@PathVariable UUID jobId, @PathVariable UUID fileId) {
        JobFileDTO jobFileDTO = jobService.deleteFile(jobId, fileId);
        return ResponseEntity.ok(jobFileDTO);
    }

    private URI generateFileURI(UUID jobId, UUID fileId) {
        return MvcUriComponentsBuilder
                .fromMethodName(JobController.class, "getJobFileDTO", jobId, fileId)
                .build().toUri();
    }

}
