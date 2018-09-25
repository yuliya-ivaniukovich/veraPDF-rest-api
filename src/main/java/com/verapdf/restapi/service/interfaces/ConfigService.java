package com.verapdf.restapi.service.interfaces;

import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Service
public interface ConfigService {
    String getSavePath();
}
