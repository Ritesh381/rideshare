package com.example.rideshare.service;

import com.example.rideshare.dto.FileUploadResponseDTO;
import com.example.rideshare.util.FileNameUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileUploadService {
    private static final String UPLOAD_DIRECTORY_NAME = "uploads";
    private final Path upload_directory;

    public FileUploadService() {
        String projectRoot =  System.getProperty("user.dir");
        // Build absolute path  --> projectRootDir/uploads
        this.upload_directory = Paths.get(projectRoot, UPLOAD_DIRECTORY_NAME);
    }

    public FileUploadResponseDTO store(MultipartFile file) {
        // 1. Validate te file
        if(file == null) {
            throw new RuntimeException("File is null");
        }

        if(file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        // 2. Generate a unique file name to prevent overwrites.
        String originalFilename = file.getOriginalFilename();

        if(originalFilename == null || originalFilename.isEmpty()) {
            throw new RuntimeException("Filename is empty or null");
        }

        String fileName = FileNameUtil.getFileName(originalFilename);

        // 3. mkdir /uploads if !exists
        try{
            if(!Files.exists(upload_directory)) {
                Files.createDirectory(upload_directory);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 4. Save the file
        Path path = upload_directory.resolve(fileName);
        File targetFile = path.toFile();

        try {
            file.transferTo(targetFile);
        } catch (IOException e) {
            throw new RuntimeException("Cannot upload file",e);
        }

        return new FileUploadResponseDTO(
                fileName, targetFile.getAbsolutePath()
        );
    }

}
