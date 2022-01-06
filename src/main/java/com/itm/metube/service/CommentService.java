package com.itm.metube.service;

import com.itm.metube.repository.VideoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CommentService {
    private final VideoRepository videoRepository;

    public void dislikeComment(String videoId,String commentId) {

    }

    public void likeComment(String videoId,String commentId) {
        System.out.println(videoRepository.findByIdAndcommentListsid(videoId,commentId));
    }
}
