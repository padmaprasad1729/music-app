package com.agap.repository;

import com.agap.model.Artist;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArtistsRepository extends MongoRepository<Artist, Long> {
    Optional<Artist> getByName(String name);
}
