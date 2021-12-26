package com.itm.metube.service;

import com.itm.metube.model.Video;
import com.itm.metube.repository.VideoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class VideoService {

    private final S3Service s3Service;
    private final VideoRepository videoRepository;

    public void uploadVideo(MultipartFile multipartFile){
       String videoUrl= s3Service.uploadFile(multipartFile);

       var video=new Video();
       video.setVideoUrl(videoUrl);

       videoRepository.save(video);

    }
}
