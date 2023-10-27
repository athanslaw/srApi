package com.edunge.bukinz.util;

import com.edunge.bukinz.exceptions.FileException;
import com.edunge.bukinz.exceptions.FileNotFoundException;
import com.edunge.bukinz.exceptions.InvalidFileException;
import com.edunge.bukinz.exceptions.MissingFieldsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);

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

    public static List<String> getCsvLines(MultipartFile file, Path storagePath) {
        if(file.getOriginalFilename().endsWith(".csv")
        ){
            List<String> lines = new ArrayList<>();
            try {
                if (file.isEmpty()) {
                    throw new FileException("Failed to store empty file.");
                }
                System.out.println("File info: "+ file);

                try (InputStream inputStream = file.getInputStream()) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    int row = 1;
                    String line;
                    int columnLength = 0;

                    while((line = bufferedReader.readLine()) != null){

                        columnLength = line.split(",").length;
                        if(row==1){
                            LOGGER.info("State header - {}", line);

                        }else {
                            lines.add(line);
                            validateColumnLength(columnLength, line.split(",").length);
                            LOGGER.info(line + " - Saved to the DB");
                        }
                        row++;
                    }

//                    Files.copy(inputStream, destinationFile,StandardCopyOption.REPLACE_EXISTING);
                }
            }
            catch (IOException e) {
                LOGGER.error(e.getMessage());
                throw new FileException("Failed to store file.", e);
            }
            return lines;
        }
        else{
            throw new InvalidFileException("You have uploaded an invalid file.");
        }
    }

    private static void validateColumnLength(Integer expected, Integer length){
        if(!length.equals(expected)){
            throw new MissingFieldsException("Invalid row");
        }
    }
}
