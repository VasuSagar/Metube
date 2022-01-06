package com.itm.metube.repository;

import com.itm.metube.model.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface VideoRepository extends MongoRepository<Video,String> {

    Page<Video> findByTitleLikeIgnoreCaseOrDescriptionLikeIgnoreCaseOrTagsInIgnoreCase(String videoTitle, String description,String tags,Pageable pageable);

//    @Query(value = "{'title': {$regex : ?0, $options: 'i'}}")
//    Page<Video> findByTitleContainingOrDescriptionContainingOrTagsContaining(String videoTitle, String description,String tag,Pageable pageable);

//    @Query(value = "{ $or: [ { 'title' : {$regex:?0,$options:'i'} }, { 'description' : {$regex:?1,$options:'i'} }] }")
//    Page<Video> findByTitleContainingOrDescriptionContainingOrTagsContaining(String videoTitle, String description,String tag,Pageable pageable);

    @Query(value = "{ 'id' : ?0, 'commentLists.id' : ?1 }", fields = "{ 'commentLists.id' : 1 }")
    List<Video> findByIdAndcommentListsid(String videoId, String commentId);

    @Query(value = "{ 'id' : {'$in' : ?0 } }", fields = "{ 'title': 1 }")
    List<Video> findHistoryBySearch(Iterable<String> ids,String search);


}
