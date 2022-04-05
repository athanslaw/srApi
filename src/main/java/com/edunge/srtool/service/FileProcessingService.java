package com.edunge.srtool.service;

import com.edunge.srtool.exceptions.FileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileProcessingService {

    private Path fileStorageLocation=null;

    public Path getFileStorageLocation(){
        return fileStorageLocation;
    }

}
