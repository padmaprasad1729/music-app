package com.agap.repository;

import com.agap.model.Song;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongsRepository extends MongoRepository<Song, Long> {
    List<Song> findByYearBefore(int year);

}
