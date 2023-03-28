/*
package com.example.demo.utils.file;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.demo.utils.file.spi.UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class FileUploadService {

    private final UploadService s3Service;

    @Transactional(timeout = 1)
    public String uploadImage(MultipartFile file){
        String fileName = createFileName(file.getOriginalFilename());
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());
        try(InputStream inputStream = file.getInputStream()){
            s3Service.uploadFile(inputStream, objectMetadata, fileName);
        }catch (IOException e){
            throw new IllegalArgumentException(String.format("file convert error"));
        }
        return s3Service.getFileUrl(fileName);
    }

    private String createFileName(String originalFileName){
        return UUID.randomUUID().toString().concat(getFileExtension(originalFileName));
    }

    private String getFileExtension(String fileName){
        try{
            return fileName.substring(fileName.lastIndexOf("."));
        }catch (StringIndexOutOfBoundsException e){
            throw new IllegalArgumentException(String.format("wrong file type : "+fileName));
        }
    }
}
*/
