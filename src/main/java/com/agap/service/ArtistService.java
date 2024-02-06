package com.agap.service;

import com.agap.exception.ResourceNotFoundException;
import com.agap.model.Artist;
import com.agap.repository.ArtistsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@CacheConfig(cacheNames={"artists"})
public class ArtistService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ArtistsRepository artistsRepository;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    public List<Artist> getAllBands() {
        return artistsRepository.findAll();
    }


    @CachePut(key = "#artistId")
    public Artist getArtistById(Long artistId)
            throws ResourceNotFoundException {
        return artistsRepository.findById(artistId)
                .orElseThrow(() -> new ResourceNotFoundException("Artist not found for this id : " + artistId));
    }

    @CachePut(key = "#artist?.id")
    public Artist createBand(Artist artist) {
        Optional<Artist> optionalArtist = artistsRepository.getByName(artist.getName());
        if (optionalArtist.isPresent()) {
            log.info("Artist already exists");
            return optionalArtist.get();
        }

        artist.setId(sequenceGeneratorService.generateSequence(Artist.SEQUENCE_NAME));
        Artist result = artistsRepository.save(artist);
        return result;
    }

    @CacheEvict(key = "#artistId")
    public Artist updateArtist(Long artistId, Artist artistDetails) throws ResourceNotFoundException {
        Artist artist = artistsRepository.findById(artistId)
                .orElseThrow(() -> new ResourceNotFoundException("Artist not found for this id : " + artistId));


        artist.setName(artistDetails.getName());

        return artistsRepository.save(artist);
    }

    @CacheEvict(key = "#artistId")
    public Map<String, Boolean> deleteArtist(Long artistId)
            throws ResourceNotFoundException {
        Artist artist = artistsRepository.findById(artistId)
                .orElseThrow(() -> new ResourceNotFoundException("Artist not found for this id : " + artistId));

        artistsRepository.delete(artist);

        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
