package com.agap.repository;

import com.agap.model.Song;
import org.springframework.stereotype.Repository;

@Repository
public interface SongSearchRepository extends ResourceRepository<Song, Long> {
}
