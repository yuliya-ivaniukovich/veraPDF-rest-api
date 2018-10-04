package com.verapdf.restapi.controller;

import com.verapdf.restapi.dto.JobDTO;
import com.verapdf.restapi.dto.JobFileDTO;
import com.verapdf.restapi.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.net.URI;
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
    public ResponseEntity<JobDTO> prepareJob() {
        JobDTO jobDTO = jobService.createJob();

        URI location = MvcUriComponentsBuilder
                .fromMethodName(JobController.class, "getJob", jobDTO.getJobId())
                .build()
                .toUri();
        return ResponseEntity.created(location).body(jobDTO);
    }

    @GetMapping(value = "/{jobId}")
    public JobDTO getJob(@PathVariable UUID jobId) {
        return jobService.getJobDTO(jobId);
    }

    @DeleteMapping(value = "/{jobId}")
    public JobDTO closeJob(@PathVariable UUID jobId) {
        return jobService.closeJob(jobId);
    }

    @PostMapping(value = "/{jobId}/files", headers = "content-type=multipart/form-data")
    public ResponseEntity<JobFileDTO> uploadFiles(@PathVariable UUID jobId, @RequestParam MultipartFile file) {
        JobFileDTO jobFileDTO = jobService.addFile(jobId, file);
        URI location = generateFileURI(jobFileDTO.getJobId(), jobFileDTO.getFileId());
        return ResponseEntity.created(location).body(jobFileDTO);
    }

    @PostMapping(value = "/{jobId}/files", headers = "content-type=application/json")
    public ResponseEntity<JobFileDTO> createPath(@PathVariable UUID jobId, @RequestBody JobFileDTO jobFileDTO) {
        JobFileDTO newJobFileDTO = jobService.addPath(jobId, jobFileDTO.getPath());
        URI location = generateFileURI(newJobFileDTO.getJobId(), newJobFileDTO.getFileId());
        return ResponseEntity.created(location).body(newJobFileDTO);
    }


    @GetMapping(value = "/{jobId}/files/{fileId}")
    public JobFileDTO getJobFile(@PathVariable UUID jobId, @PathVariable UUID fileId) {
        return jobService.getJobFile(jobId, fileId);
    }

    @DeleteMapping("/{jobId}/files/{fileId}")
    public JobFileDTO deleteJobFile(@PathVariable UUID jobId, @PathVariable UUID fileId) {
        return jobService.deleteFile(jobId, fileId);
    }

    private URI generateFileURI(UUID jobId, UUID fileId) {
        return MvcUriComponentsBuilder
                .fromMethodName(JobController.class, "getJobFile", jobId, fileId)
                .build().toUri();
    }

}
