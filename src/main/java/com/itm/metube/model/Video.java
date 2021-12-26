package com.itm.metube.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Set;

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
    private Integer likesCount;
    private Integer dislikesCount;
    private Integer viewCount;
    private Set<String> tags;
    private String videoUrl;
    private String thumbnailUrl;
    private VideoStatus videoStatus;
    private List<Comment> commentList;

}
