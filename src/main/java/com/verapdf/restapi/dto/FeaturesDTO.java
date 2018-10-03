package com.verapdf.restapi.dto;

import java.util.List;

public class FeaturesDTO {

    private List<String> features;

    public FeaturesDTO(List<String> features) {
        setFeatures(features);
    }

    public List<String> getFeatures() {
        return features;
    }

    public void setFeatures(List<String> features) {
        this.features = features;
    }
}
