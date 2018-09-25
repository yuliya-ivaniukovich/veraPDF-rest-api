package com.verapdf.restapi.service.implementations;

import com.verapdf.restapi.service.interfaces.ConfigService;
import org.springframework.stereotype.Service;


@Service
public class ConfigServiceImpl implements ConfigService {

    public String getSavePath() {
        return "work";
    }

}
