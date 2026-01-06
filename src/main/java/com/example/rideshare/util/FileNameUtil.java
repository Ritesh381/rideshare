package com.example.rideshare.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class FileNameUtil {
    public static String getFileName(String originalFileName) {
        if(originalFileName == null || originalFileName.isEmpty()){
            throw new RuntimeException("fileName is null or empty");
        }

        int index = originalFileName.lastIndexOf('.');
        if(index == -1){
            return UUID.randomUUID().toString() + "."  + originalFileName;
        }
        String extension = originalFileName.substring(index + 1);
        return UUID.randomUUID().toString() + originalFileName + "." + extension;
    }
}
