package com.verapdf.restapi.service;

import ch.qos.logback.core.util.FileUtil;
import com.verapdf.restapi.entity.Job;
import com.verapdf.restapi.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.UUID;


@Service
public class JobService {

    private static final Logger log = LoggerFactory.getLogger(JobService.class);

    private HashMap<UUID, Job> jobHashMap;

    private ConfigService configService;

    @Autowired
    public JobService(ConfigService configService) {
        jobHashMap = new HashMap<>();
        this.configService = configService;
    }

    public void addJob(Job job) {
        Path path = Paths.get(System.getProperty("user.dir"), configService.getSavePath(), File.separator, job.getJobId().toString());
        FileUtils.createDirectory(path);
        jobHashMap.put(job.getJobId(), job);
    }

    public Job getJob(UUID uuid) {
      return jobHashMap.get(uuid);
    }

    public void setFiles(Job job, MultipartFile[] files) {
        OutputStream outputStream = null;
        InputStream inputStream = null;

        for (MultipartFile file : files) {
            Path filePath = Paths.get(System.getProperty("user.dir"), configService.getSavePath(),
                    job.getJobId().toString(), file.getOriginalFilename());

            try {
                inputStream = file.getInputStream();
                File newFile = new File(filePath.toString());
                outputStream = new FileOutputStream(newFile);
                int read;
                byte[] bytes = new byte[1024];
                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }
                job.addFile(newFile, true);
            } catch (IOException e) {
                log.error("Unable to transfer file.");
            }
            finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        log.error("Error closing input stream.");
                    }
                }
                if (outputStream != null) {
                    try {
                        // outputStream.flush();
                        outputStream.close();
                    } catch (IOException e) {
                        log.error("Error closing output stream.");
                    }

                }
            }
        }
    }

    public void deleteFiles(Job job, String[] fileNames) {
        for (String fileName : fileNames) {
            Path filePath = Paths.get(System.getProperty("user.dir"), configService.getSavePath(),
                    job.getJobId().toString(), fileName);
            try {
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                log.error("Unable to delete file.");
            }
        }
    }

    public void setPaths(Job job, String[] paths) {
        for (String path : paths) {
            if (Files.exists(Paths.get(path))){
                job.addFile(new File(path), false);
            }
            else {
                log.debug("File not exist");
            }
        }
    }

    public void closeJob(Job job) {
        Path filePath = Paths.get(System.getProperty("user.dir"), configService.getSavePath(),
                job.getJobId().toString());
        job.close();
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            log.error("Unable to delete directory");
        }
    }

}
