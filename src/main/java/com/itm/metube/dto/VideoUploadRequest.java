package com.itm.metube.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoUploadRequest {
    private MultipartFile videoFile;
    private MultipartFile imageFile;
    private String videoTitle;
    private String description;
    private String tags;
    private Integer videoLength;
}
