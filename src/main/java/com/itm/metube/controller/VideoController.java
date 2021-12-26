package com.itm.metube.controller;

import com.itm.metube.model.User;
import com.itm.metube.service.VideoService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@AllArgsConstructor
public class VideoController {
    private final VideoService videoService;

    @PostMapping("/addVideo")
    @ResponseStatus(HttpStatus.CREATED)
    public void uploadVideo(@RequestParam("file")MultipartFile file){
        videoService.uploadVideo(file);
    }

    @GetMapping("/test")
    public void getUser(){
        System.out.println("TEST");
    }
}
