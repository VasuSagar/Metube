package com.itm.metube.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    String uploadFile(MultipartFile multipartFile);

    void deleteFile(String videoUrl);
}
