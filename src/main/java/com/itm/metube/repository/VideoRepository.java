package com.itm.metube.repository;

import com.itm.metube.model.Video;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VideoRepository extends MongoRepository<Video,String> {
}
