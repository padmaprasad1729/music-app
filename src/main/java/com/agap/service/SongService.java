package com.agap.service;

import com.agap.exception.ResourceNotFoundException;
import com.agap.model.Artist;
import com.agap.model.FilterCondition;
import com.agap.model.PageResponse;
import com.agap.model.Song;
import com.agap.repository.SongSearchRepository;
import com.agap.repository.SongsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@CacheConfig(cacheNames={"songs"})
public class SongService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SongsRepository songsRepository;

    @Autowired
    private SongSearchRepository songSearchRepository;

    @Autowired
    private FilterBuilderService filterBuilderService;

    @Autowired
    private ArtistService artistService;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    public List<Song> getAllSongs() {
        return songsRepository.findAll();
    }

    @Cacheable(key = "#songId")
    public Song getSongById(Long songId)
            throws ResourceNotFoundException {
        return songsRepository.findById(songId)
                .orElseThrow(() -> new ResourceNotFoundException("Song not found for this id : " + songId));
    }

    public List<Song> getSongBeforeYear(int year) {
        return songsRepository.findByYearBefore(year);
    }

    @Cacheable()
    public Song createSong(Song song) {
        Artist artist = artistService.createBand(new Artist(1, song.getArtist()));
        log.info("Created artist when creating song: {}", artist.getName());

        song.setId(sequenceGeneratorService.generateSequence(Song.SEQUENCE_NAME));
        Song result = songsRepository.save(song);
        return result;
    }

    @CachePut(key = "#songId")
    public Song updateSong(Long songId, Song songDetails) throws ResourceNotFoundException {
        Song song = songsRepository.findById(songId)
                .orElseThrow(() -> new ResourceNotFoundException("Song not found for this id : " + songId));

        song.setName(songDetails.getName());
        song.setYear(songDetails.getYear());
        song.setArtist(songDetails.getArtist());
        song.setShortname(songDetails.getShortname());
        song.setGenre(songDetails.getGenre());
        song.setAlbum(songDetails.getAlbum());

        return songsRepository.save(song);
    }

    @CacheEvict(key = "#songId")
    public Map<String, Boolean> deleteSong(Long songId)
            throws ResourceNotFoundException {
        Song song = songsRepository.findById(songId)
                .orElseThrow(() -> new ResourceNotFoundException("Song not found for this id : " + songId));

        songsRepository.delete(song);

        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }

    public List<Song> querySongs(Query query) {
        return songSearchRepository.findAll(query);
    }

    public PageResponse<Song> querySongsPageable(Query query, Pageable pageable) {
        PageResponse<Song> response = new PageResponse<>();
        Page<Song> pg = songSearchRepository.findAll(query, pageable);
        response.setPageStats(pg, pg.getContent());
        return response;
    }

    public Pageable createPageable(int size, int page, String orders) {
        return filterBuilderService.getPageable(size, page, orders);
    }

    public Query createQuery(String filterOr, String filterAnd) {
        GenericFilterCriteriaBuilder filterCriteriaBuilder = new GenericFilterCriteriaBuilder();

        List<FilterCondition> andConditions = filterBuilderService.createFilterCondition(filterAnd);
        List<FilterCondition> orConditions = filterBuilderService.createFilterCondition(filterOr);

        return filterCriteriaBuilder.addCondition(andConditions, orConditions);
    }
}
