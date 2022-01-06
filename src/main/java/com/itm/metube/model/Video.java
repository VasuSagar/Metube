package com.itm.metube.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(value="video")
@Builder
public class Video {
    @Id
    private String id;
    private String title;
    private String description;
    private String userId;
    private String authorName;
    private String authorProfilePhotoUrl;
    private AtomicInteger likes = new AtomicInteger(0);
    private AtomicInteger disLikes = new AtomicInteger(0);
    private AtomicInteger viewCount = new AtomicInteger(0);
    private Set<String> tags;
    private String videoUrl;
    private String thumbnailUrl;
    private VideoStatus videoStatus;
    private Integer videoLength; //seconds
    private List<Comment> commentList=new ArrayList<>();

    public int likeCount() {
        return likes.get();
    }

    public int disLikeCount() {
        return disLikes.get();
    }

    public void increaseViewCount() {
        viewCount.incrementAndGet();
    }

    public void increaseLikeCount() {
        likes.incrementAndGet();
    }

    public void decreaseLikeCount() {
        likes.decrementAndGet();
    }

    public void increaseDisLikeCount() {
        disLikes.incrementAndGet();
    }

    public void decreaseDisLikeCount() {
        disLikes.decrementAndGet();
    }

    public void addComment(Comment comment) {
        commentList.add(comment);
    }

}
