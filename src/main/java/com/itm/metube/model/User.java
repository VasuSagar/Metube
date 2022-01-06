package com.itm.metube.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "user")
@Builder
public class User {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Set<String> subscribedUser = new HashSet<>();
    private Set<String> subscribers = new HashSet<>();
    private Set<String> likedVideos = new HashSet<>();
    private Set<String> disLikedVideos = new HashSet<>();
    private Set<String> likedComments = new HashSet<>();
    private Set<String> disLikedComments = new HashSet<>();
    private Set<String> videoHistory=new LinkedHashSet<>();;
    private Instant created;
    private String profilePhotoUrl;

    public void addToLikedVideos(String videoId) {
        likedVideos.add(videoId);
    }

    public void removeFromLikedVideos(String videoId) {
        likedVideos.remove(videoId);
    }

    public void addToDisLikedVideo(String videoId) {
        disLikedVideos.add(videoId);
    }

    public void addToHistory(String videoId) {
        videoHistory.add(videoId);
    }

    public void removeFromHistory(String videoId) {
        videoHistory.remove(videoId);
    }

    public void removeFromDisLikedVideo(String videoId) {
        disLikedVideos.remove(videoId);
    }

    public void addToLikedComments(String commentId) {
        likedComments.add(commentId);
    }

    public void removeFromLikedComments(String commentId) {
        likedComments.remove(commentId);
    }

    public void addToDislikedComments(String commentId) {
        disLikedComments.add(commentId);
    }

    public void removeFromDisLikedComments(String commentId) {
        disLikedComments.remove(commentId);
    }

    public void addToSubscribedUsers(String userId) {
        subscribedUser.add(userId);
    }

    public void remoeFromSubscribedUsers(String userId) {
        subscribedUser.remove(userId);
    }

    public void addToSubscribers(String userId) {
        subscribers.add(userId);
    }

    public void removeFromSubscribers(String userId) {
        subscribers.remove(userId);
    }

    public void addToVideoHistory(String videoId) {
        videoHistory.add(videoId);
    }


}
