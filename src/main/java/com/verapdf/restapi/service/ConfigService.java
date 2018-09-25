package com.verapdf.restapi.service;

import org.springframework.stereotype.Service;


@Service
public class ConfigService{

    String getSavePath() {
        return "Jobs";
    }

}
