package com.edunge.bukinz.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.nio.file.Path;

@Service
public class FileProcessingService {

    private Path fileStorageLocation=null;

    public Path getFileStorageLocation(){
        return fileStorageLocation;
    }

}
