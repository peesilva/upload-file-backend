package com.example.fileupload.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Service
public class FileUploadService {

    private final S3Service s3Service;

    @Autowired
    public FileUploadService(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    public String uploadFile(MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            s3Service.uploadFile(fileName, file);
            return "Upload do arquivo com sucesso.";
        }
        catch (IOException e) {
            throw new RuntimeException("Opaaa!!! o upload deu errado.");
        }
    }
}