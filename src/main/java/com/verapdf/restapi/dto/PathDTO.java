package com.verapdf.restapi.dto;

public class PathDTO {

    private JobFileDTO.FileType type;
    private String path;

    public PathDTO() { }

    public PathDTO(JobFileDTO.FileType type, String path) {
        this.type = type;
        this.path = path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public JobFileDTO.FileType getType() {
        return type;
    }

    public void setType(JobFileDTO.FileType type) {
        this.type = type;
    }
}
