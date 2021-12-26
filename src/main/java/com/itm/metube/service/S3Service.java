package com.itm.metube.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.UUID;

@Service
@AllArgsConstructor
public class S3Service implements FileService{

    private final AmazonS3Client amazonS3Client;


    @Override
    public String uploadFile(MultipartFile multipartFile){
        var key= UUID.randomUUID().toString();
        var metadata=new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        try{
            amazonS3Client.putObject("metube-vasu",key,multipartFile.getInputStream(),metadata);
        }
        catch (IOException ioException){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Exception occured while uploading to s3");
        }

        //amazonS3Client.setObjectAcl("metube-vasu",key, CannedAccessControlList.PublicRead);

        return amazonS3Client.getResourceUrl("metube-vasu",key);

    }
}
