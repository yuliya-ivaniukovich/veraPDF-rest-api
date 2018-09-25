package com.verapdf.restapi.service.interfaces;

import com.verapdf.restapi.entity.Job;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface JobService {
    void addJob(Job job);
    Job getJob(UUID uuid);
}
