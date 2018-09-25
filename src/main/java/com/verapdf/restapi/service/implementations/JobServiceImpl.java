package com.verapdf.restapi.service.implementations;

import com.verapdf.restapi.entity.Job;
import com.verapdf.restapi.service.interfaces.ConfigService;
import com.verapdf.restapi.service.interfaces.JobService;
import com.verapdf.restapi.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.UUID;


@Service
public class JobServiceImpl implements JobService {
    private HashMap<UUID, Job> jobHashMap;

    private ConfigService configService;

    @Autowired
    public JobServiceImpl(ConfigService configService) {
        jobHashMap = new HashMap<>();
        this.configService = configService;
    }

    @Override
    public void addJob(Job job) {
        Path path = Paths.get(configService.getSavePath(), File.separator, job.getJobId().toString());
        FileUtils.createDirectory(path);
        jobHashMap.put(job.getJobId(), job);
    }

    @Override
    public Job getJob(UUID uuid) {
      return jobHashMap.get(uuid);
    }

}
