package com.verapdf.restapi.controller;

import com.verapdf.restapi.dto.FeaturesDTO;
import com.verapdf.restapi.dto.ProfilesDTO;
import com.verapdf.restapi.service.VeraPDFService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/vera")
public class VeraPDFController {

    private VeraPDFService veraPDFService;

    @Autowired
    public VeraPDFController(VeraPDFService veraPDFService) {
        this.veraPDFService = veraPDFService;
    }

    @GetMapping(value = "/features")
    public List<String> getFeatures() {
        return veraPDFService.getFeatureList();
    }

    @GetMapping(value = "/profiles")
    public List<String> getProfiles() {
        return veraPDFService.getProfilesList();
    }
}
