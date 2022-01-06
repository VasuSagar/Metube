package com.itm.metube.controller;

import com.itm.metube.dto.CommentDto;
import com.itm.metube.dto.VideoDto;
import com.itm.metube.dto.VideoUploadRequest;
import com.itm.metube.model.Comment;
import com.itm.metube.model.User;
import com.itm.metube.model.Video;
import com.itm.metube.service.CommentService;
import com.itm.metube.service.VideoService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/videos")
@AllArgsConstructor
public class VideoController {
    private final VideoService videoService;
    private final CommentService commentService;

//    public void uploadVideo(@RequestParam("file")MultipartFile file){
//        videoService.uploadVideo(file);
//    }
    @PostMapping("/uploadVideo")
    @ResponseStatus(HttpStatus.CREATED)
    public void uploadVideo(@ModelAttribute VideoUploadRequest videoUploadRequest){
        videoService.uploadVideo(videoUploadRequest);
    }

    @PostMapping("/uploadThumbnail")
    @ResponseStatus(HttpStatus.CREATED)
    public void uploadThumbnail(@RequestParam("file")MultipartFile file,@RequestParam("videoId") String videoId){
        videoService.uploadThumbnail(file,videoId);
    }

    @PutMapping("/editVideo")
    @ResponseStatus(HttpStatus.OK)
    public void editVideoData(@RequestBody VideoDto videoDto){
        videoService.editVideoData(videoDto);
    }

    @PostMapping("/getAllVideos")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Map<String, Object>> getAllVideos(@RequestParam(required = false) String search,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "30") int size) {
        System.out.println(search);
       return videoService.getAllVideos(search,page,size);
    }

    @GetMapping("/getVideoById/{videoId}")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> getVideoById(@PathVariable String videoId, @RequestHeader(name = "Authorization", required = false) String authorization) {

        if(authorization!=null){
            return videoService.getVideoById(videoId,true);
        }
        else{
            return videoService.getVideoById(videoId,false);
        }

    }

    @PostMapping("/likeVideo/{videoId}")
    @ResponseStatus(HttpStatus.OK)
    public void likeVideo(@PathVariable String videoId) {
        videoService.like(videoId);
    }

    @PostMapping("/dislikeVideo/{videoId}")
    @ResponseStatus(HttpStatus.OK)
    public void disLikeVideo(@PathVariable String videoId) {
        videoService.dislike(videoId);
    }

    @PostMapping("/addComments")
    @ResponseStatus(HttpStatus.CREATED)
    public void addComments(@RequestBody CommentDto commentDto) {
        videoService.addComment(commentDto);
    }

    @GetMapping("/getAllComments/{videoId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Comment> getAllComments(@PathVariable String videoId) {
        return videoService.getAllComments(videoId);
    }

}
