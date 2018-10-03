package com.verapdf.restapi.dto;

import java.util.List;

public class ProfilesDTO {

    private List<String> profiles;

    public ProfilesDTO(List<String> features) {
        setProfiles(features);
    }

    public List<String> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<String> profiles) {
        this.profiles = profiles;
    }
}
