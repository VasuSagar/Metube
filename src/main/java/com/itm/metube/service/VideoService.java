package com.itm.metube.service;

import com.itm.metube.dto.CommentDto;
import com.itm.metube.dto.VideoDto;
import com.itm.metube.dto.VideoUploadRequest;
import com.itm.metube.model.Comment;
import com.itm.metube.model.User;
import com.itm.metube.model.Video;
import com.itm.metube.model.VideoStatus;
import com.itm.metube.repository.VideoRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.*;

@Service
@AllArgsConstructor
public class VideoService {

    private final S3Service s3Service;
    private final VideoRepository videoRepository;
    private final AuthService authService;
    private final UserService userService;

    public void uploadVideo(VideoUploadRequest videoUploadRequest){

        if(videoUploadRequest.getVideoFile()==null || videoUploadRequest.getImageFile()==null){
            System.out.println("NULL FILE");
        }

        else if(checkSizeValidation(videoUploadRequest.getVideoFile(),videoUploadRequest.getImageFile())){
            String videoUrl= s3Service.uploadFile(videoUploadRequest.getVideoFile());
            String thumbnailUrl= s3Service.uploadFile(videoUploadRequest.getImageFile());
            Video video=new Video(); //var use


            video.setVideoUrl(videoUrl);
            video.setThumbnailUrl(thumbnailUrl);
            video.setTitle(videoUploadRequest.getVideoTitle());
            video.setVideoStatus(VideoStatus.PUBLIC);
            video.setVideoLength(videoUploadRequest.getVideoLength());
            User user=authService.getCurrentUser();
            video.setUserId(user.getId());
            video.setAuthorName(user.getFirstName()+" "+user.getLastName());
            video.setAuthorProfilePhotoUrl(user.getProfilePhotoUrl());

            //set of string tags
            String[] tagsArr = videoUploadRequest.getTags().split(",");
            Set<String> set = new HashSet<>(Arrays.asList(tagsArr));
            video.setTags(set);

            video.setDescription(videoUploadRequest.getDescription());

            videoRepository.save(video);
        }





    }

    private boolean checkSizeValidation(MultipartFile videoFile, MultipartFile imageFile) {
        if(imageFile!=null){
            //transform in MB
            long imageSizeInMb = imageFile.getSize()/ (1024 * 1024);
            System.out.println("image"+imageFile.getSize());
            if(imageSizeInMb<=10){
                return true;
            }
            else{
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Size greater than 10 mb");
                //return false;
            }
        }
        long videoSizeInMb = videoFile.getSize()/ (1024 * 1024);
        System.out.println("videoSizeInMb"+videoFile.getSize());
        if(videoSizeInMb<=10){
            return true;
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Size greater than 10 mb");
        //return false;

    }

    public void editVideoData(VideoDto videoDto) {
        Video video=videoRepository.findById(videoDto.getId()).orElseThrow(()->new IllegalArgumentException("Cannot find video of id:+"+videoDto.getId()));
        //if video's uploader and current user does not match then return false;
        if(video.getUserId().equals(authService.getCurrentUser().getId())){
            video.setTitle(videoDto.getTitle());
            video.setDescription(videoDto.getDescription());
            video.setTags(videoDto.getTags());
            video.setThumbnailUrl(videoDto.getThumbnailUrl());

            videoRepository.save(video);
        }
        else{

        }

    }

    public void uploadThumbnail(MultipartFile file, String videoId) {
        //check if video exists
        Video video=videoRepository.findById(videoId).orElseThrow(()-> new IllegalArgumentException("no video found of id:"+videoId));

        //upload image
       String thumbnailUrl=s3Service.uploadFile(file);

       video.setThumbnailUrl(thumbnailUrl);

       videoRepository.save(video);


    }

    public ResponseEntity<Map<String, Object>> getAllVideos(String search, int page, int size) {

        List<Video> videos = new ArrayList<Video>();
        Pageable paging = PageRequest.of(page, size);

        Page<Video> pageVideos;
        if(search==null){
            pageVideos=videoRepository.findAll(paging);
        }
        else{
            pageVideos=videoRepository.findByTitleLikeIgnoreCaseOrDescriptionLikeIgnoreCaseOrTagsInIgnoreCase(search,search,search,paging);
        }
        videos=pageVideos.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("videos", videos);
        response.put("currentPage", pageVideos.getNumber());
        response.put("totalItems", pageVideos.getTotalElements());
        response.put("totalPages", pageVideos.getTotalPages());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public Map<String, Object> getVideoById(String videoId, boolean isUserLoggedIn) {
        Video video=videoRepository.findById(videoId).orElseThrow(()->new IllegalArgumentException("No video found of id:"+videoId));
        Map<String, Object> response = new HashMap<>();

        video.increaseViewCount();
        videoRepository.save(video);


        if(isUserLoggedIn){
            //add to history
            userService.addToHistory(videoId);


            //get liked dislikes of this video
            boolean hasUserLikedVideo=userService.userHasLikedVideo(videoId);
            boolean hasUserdisLikedVideo=userService.userHasDislikedVideo(videoId);

            response.put("hasUserLikedVideo", hasUserLikedVideo);
            response.put("hasUserdisLikedVideo", hasUserdisLikedVideo);
        }
        response.put("video", video);
        return response;
    }

    public void deleteVideo(String videoId) {
        Video video=videoRepository.findById(videoId).orElseThrow(()-> new IllegalArgumentException("no video found of id:"+videoId));
        String videoUrl=video.getVideoUrl();
        s3Service.deleteFile(videoUrl);
    }

    public void like(String videoId) {
        Video video=videoRepository.findById(videoId).orElseThrow(()-> new IllegalArgumentException("no video found of id:"+videoId));
        if(userService.userHasLikedVideo(videoId)){
            //now since user has liked this(and user can only like or dislike)
            //so we will make this video neutral(0 like 0dislike)

            //remove from users likedVideos
            userService.removeFromLikedVideos(videoId);

            video.decreaseLikeCount();

        }
        else if(userService.userHasDislikedVideo(videoId)){
            video.decreaseDisLikeCount();

            userService.removeFromDislikedVideos(videoId);

            //like video
            userService.addToLikedVideos(videoId);

            video.increaseLikeCount();
        }
        else{
            //video is neither liked nor disliked

            //like video and increase count
            userService.addToLikedVideos(videoId);

            video.increaseLikeCount();
        }

        videoRepository.save(video);


    }

    public void dislike(String videoId) {
        Video video=videoRepository.findById(videoId).orElseThrow(()-> new IllegalArgumentException("no video found of id:"+videoId));

        if(userService.userHasLikedVideo(videoId)){
            userService.removeFromLikedVideos(videoId);
            video.decreaseLikeCount();

            userService.addToDislikedVideos(videoId);
            video.increaseDisLikeCount();


        }
        else if(userService.userHasDislikedVideo(videoId)){
            userService.removeFromDislikedVideos(videoId);
            video.decreaseDisLikeCount();
        }
        else{
            userService.addToDislikedVideos(videoId);

            video.increaseDisLikeCount();
        }

        videoRepository.save(video);
    }

    public void addComment(CommentDto commentDto) {
        Video video=videoRepository.findById(commentDto.getVideoId()).orElseThrow(()-> new IllegalArgumentException("no video found of id:"+commentDto.getVideoId()));
        Comment comment=new Comment();
        User user=authService.getCurrentUser();
        comment.setAuthorId(user.getId());
        comment.setAuthorName(user.getFirstName()+" "+user.getLastName());
        comment.setText(commentDto.getText());
        comment.setAuthorProfilePhotoUrl(user.getProfilePhotoUrl());
        comment.setCreated(Instant.now());

        video.addComment(comment);

        videoRepository.save(video);

    }

    public List<Comment> getAllComments(String videoId) {
        Video video=videoRepository.findById(videoId).orElseThrow(()-> new IllegalArgumentException("no video found of id:"+videoId));
       List<Comment> comments=video.getCommentList();
       return comments;
    }


}
