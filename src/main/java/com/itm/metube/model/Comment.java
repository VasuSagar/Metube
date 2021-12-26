package com.itm.metube.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Comment {
    @Id
    private String id;
    private String authorId;
    private String text;
    private Integer likesCount;
    private Integer dislikesCount;
}
