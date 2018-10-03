package com.verapdf.restapi.controller;

import com.verapdf.restapi.dto.FeaturesDTO;
import com.verapdf.restapi.dto.ProfilesDTO;
import com.verapdf.restapi.service.VeraPDFService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vera")
public class VeraPDFController {

    private VeraPDFService veraPDFService;

    @Autowired
    public VeraPDFController(VeraPDFService veraPDFService) {
        this.veraPDFService = veraPDFService;
    }

    @GetMapping(value = "/features")
    public ResponseEntity<FeaturesDTO> getFeatures() {
        FeaturesDTO featuresDTO = new FeaturesDTO(veraPDFService.getFeatureList());
        return ResponseEntity.ok().body(featuresDTO);
    }

    @GetMapping(value = "/profiles")
    public ResponseEntity<ProfilesDTO> getProfiles() {
        ProfilesDTO profilesDTO = new ProfilesDTO(veraPDFService.getProfilesList());
        return ResponseEntity.ok().body(profilesDTO);
    }
}
