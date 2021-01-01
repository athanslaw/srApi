package com.edunge.srtool.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "file")
public class FileConfigurationProperties {
    private String uploadDir;
    private String svgDir;

    public String getSvgDir() {
        return svgDir;
    }

    public void setSvgDir(String svgDir) {
        this.svgDir = svgDir;
    }

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }
}
