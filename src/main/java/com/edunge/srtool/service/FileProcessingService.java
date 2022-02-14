package com.edunge.srtool.service;

import com.edunge.srtool.config.FileConfigurationProperties;
import com.edunge.srtool.exceptions.FileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileProcessingService {

    @Autowired
    FileConfigurationProperties fileConfigurationProperties;
    private Path fileStorageLocation;

    @PostConstruct
    void init(){
        try {
            this.fileStorageLocation = Paths.get(fileConfigurationProperties.getSvgDir())
                    .toAbsolutePath().normalize();
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileNotFoundException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public Path getFileStorageLocation(){
        return fileStorageLocation;
    }

}
