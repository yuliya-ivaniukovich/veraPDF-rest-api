package com.verapdf.restapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vera")
public class VeraPDFController {

    @GetMapping(value = "/features")
    public ResponseEntity<String> getFeatures() {
        return ResponseEntity.ok().body("features");
    }

    @GetMapping(value = "/profiles")
    public ResponseEntity<String> getProfiles() {
        return ResponseEntity.ok().body("profiles");
    }
}
