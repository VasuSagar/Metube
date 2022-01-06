package com.itm.metube.service;

import com.itm.metube.model.User;
import com.itm.metube.model.Video;
import com.itm.metube.repository.UserRepository;
import com.itm.metube.repository.VideoRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AuthService authService;
    private final VideoRepository videoRepository;

    public boolean userHasLikedVideo(String videoId) {
        return authService.getCurrentUser().getLikedVideos().stream().anyMatch(id -> id.equals(videoId));
    }

    public boolean userHasDislikedVideo(String videoId) {
        return authService.getCurrentUser().getDisLikedVideos().stream().anyMatch(id -> id.equals(videoId));
    }

    public void removeFromLikedVideos(String videoId) {
       User user= authService.getCurrentUser();
       user.removeFromLikedVideos(videoId);
       userRepository.save(user);
    }

    public void removeFromDislikedVideos(String videoId) {
       User user= authService.getCurrentUser();
       user.removeFromDisLikedVideo(videoId);
       userRepository.save(user);
    }

    public void addToLikedVideos(String videoId) {
        User user= authService.getCurrentUser();
        user.addToLikedVideos(videoId);
        userRepository.save(user);
    }

    public void addToDislikedVideos(String videoId) {
        User user= authService.getCurrentUser();
        user.addToDisLikedVideo(videoId);
        userRepository.save(user);
    }

    //comments

    public void addToLikedComments(String commentId) {
        User user= authService.getCurrentUser();
        user.addToLikedComments(commentId);
        userRepository.save(user);
    }

    public void addToDislikedComments(String commentId) {
        User user= authService.getCurrentUser();
        user.addToDislikedComments(commentId);
        userRepository.save(user);
    }

    public void removeFromLikedComments(String commentId) {
        User user= authService.getCurrentUser();
        user.removeFromLikedComments(commentId);
        userRepository.save(user);
    }

    public void removeFromDislikedComments(String commentId) {
        User user= authService.getCurrentUser();
        user.removeFromDisLikedComments(commentId);
        userRepository.save(user);
    }

    public boolean userHasLikedComment(String commentId) {
        return authService.getCurrentUser().getLikedComments().stream().anyMatch(id -> id.equals(commentId));
    }

    public boolean userHasDislikedComment(String commentId) {
        return authService.getCurrentUser().getDisLikedComments().stream().anyMatch(id -> id.equals(commentId));
    }

    public void subscribeUser(String userId) {
        User currentUser =authService.getCurrentUser();
        currentUser.addToSubscribedUsers(userId);
        User subscribedToUser = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found :" + userId));
        subscribedToUser.addToSubscribers(currentUser.getId());
        userRepository.save(currentUser);
        userRepository.save(subscribedToUser);
    }

    public void unsubscribe(String userId){
        User currentUser =authService.getCurrentUser();
        currentUser.remoeFromSubscribedUsers(userId);
        User subscribedToUser = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found :" + userId));
        subscribedToUser.removeFromSubscribers(currentUser.getId());
        userRepository.save(currentUser);
        userRepository.save(subscribedToUser);
    }


    public Map<String, Object> getAllSubscribedUsers() {
        User currentUser =authService.getCurrentUser();
        Set<String> subscriptions= currentUser.getSubscribedUser();

        //get name,id & photo of subscriptions
        List<User> subscriptionUser = new ArrayList<>();

        for(String subscription:subscriptions){
            User user=userRepository.findById(subscription).orElseThrow(()->new IllegalArgumentException("no user id with:"+subscription));
            subscriptionUser.add(user);
        }

        Map<String, Object> response = new HashMap<>();

        response.put("subscriptions",subscriptionUser);

        return response;

    }

    public void addToHistory(String videoId) {
        User user=authService.getCurrentUser();

        user.addToHistory(videoId);

        userRepository.save(user);

    }


    public ResponseEntity<Map<String, Object>> getAllHistoryVideos(String search) {
        List<Video> videos = new ArrayList<Video>();
        User user=authService.getCurrentUser();
        Set<String> histories=user.getVideoHistory();
        if(search==null){


            videos= (List<Video>)videoRepository.findAllById(histories);
        }

        else{
            videos=videoRepository.findHistoryBySearch(histories,search);
        }


        Map<String, Object> response = new HashMap<>();
        response.put("videos", videos);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
