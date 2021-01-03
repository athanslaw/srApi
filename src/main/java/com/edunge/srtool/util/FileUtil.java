package com.edunge.srtool.util;

import com.edunge.srtool.exceptions.FileException;
import com.edunge.srtool.exceptions.FileNotFoundException;
import com.edunge.srtool.exceptions.InvalidFileException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

public class FileUtil {
    public static String uploadFile(MultipartFile file, Path storagePath){
        if(Objects.requireNonNull(file.getOriginalFilename()).endsWith(".jpg")
                || file.getOriginalFilename().endsWith(".png")
                || file.getOriginalFilename().endsWith(".jpeg")
                || file.getOriginalFilename().endsWith(".svg")
        ){
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            try {
                // Check if the file's name contains invalid characters
                if(fileName.contains("..")) {
                    throw new FileException("Sorry! Filename contains invalid path sequence " + fileName);
                }
                // Copy file to the target location (Replacing existing file with the same name)
                Path targetLocation = storagePath.resolve(fileName);
                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

                return fileName;
            } catch (IOException ex) {
                throw new FileException("Could not store file " + fileName + ". Please try again!", ex);
            }
        }
        else{
            throw new InvalidFileException("You have uploaded an invalid file.");
        }
    }

    public static Resource loadResource(String fileName, Path storageLocation) {
        try {
            Path filePath = storageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new FileNotFoundException("File not found " + fileName, ex);
        }
    }
}
