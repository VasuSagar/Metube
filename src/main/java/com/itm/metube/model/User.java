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
@Document(value = "user")
@Builder
public class User {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Set<String> subscribedUser;
    private Set<String> subscribers;
    private Set<String> likedVideos;
    private Set<String> disLikedVideos;
    private List<String> videoHistory;

}
