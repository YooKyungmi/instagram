/*
package com.example.demo.utils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ListIterator;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AmazonS3Utils {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3 s3;

    //파일 업로드
    public String uploadFile(String folderName, MultipartFile multipartFile) throws IOException {
        File uploadFile = convert(multipartFile)
                .orElseThrow(()->new IllegalArgumentException("MultipartFile 형식을 File로 전환하는 데에 실패하였습니다."));
        return upload(uploadFile, folderName);
    }

    //파일 삭제
    public void deleteFile(String fileName){
        DeleteObjectRequest request = new DeleteObjectRequest(bucket, fileName);
        s3.deleteObject(request);
    }

    //폴더 삭제 (폴더안의 모든 파일 삭제)
    public void removeFolder(String folderName){
        ListObjectsV2Request listObjectsV2Request = new ListObjectsV2Request().withBucketName(bucket).withPrefix(folderName+"/");
        ListObjectsV2Result listObjectsV2Result = s3.listObjectsV2(listObjectsV2Request);
        ListIterator<S3ObjectSummary> listIterator = listObjectsV2Result.getObjectSummaries().listIterator();

        while (listIterator.hasNext()){
            S3ObjectSummary objectSummary = listIterator.next();
            DeleteObjectRequest request = new DeleteObjectRequest(bucket,objectSummary.getKey());
            s3.deleteObject(request);
            System.out.println("Deleted " + objectSummary.getKey());
        }
    }

    private String upload(File uploadFile, String folderName){
        String fileName = folderName + "/" + uploadFile.getName();
        String uploadImageUrl = putS3(uploadFile, fileName);

        removeNewFile(uploadFile);
        return uploadImageUrl;
    }

    //S3에 업로드
    private String putS3(File uploadFile, String fileName){
        s3.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        return s3.getUrl(bucket, fileName).toString();
    }

    //임시로 생성된 new file을 삭제해준다
    private void removeNewFile(File targetFile){
        if(targetFile.delete()){
            System.out.println("File deleted.");
        }else{
            System.out.println("File doesn't deleted.");
        }
    }

    //multipartFile을 File타입으로 변환해준다. (변환된 파일을 가지고 put해줄 것이다)
    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(System.currentTimeMillis() + StringUtils.cleanPath(file.getOriginalFilename()));

        if(convertFile.createNewFile()){
            try(FileOutputStream fos = new FileOutputStream(convertFile)){
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }


}*/
