package com.agap.controller;

import com.agap.exception.ResourceNotFoundException;
import com.agap.model.PageResponse;
import com.agap.model.Song;
import com.agap.service.SongService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class SongController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SongService songService;

    @GetMapping(value = "/songs", headers = "Accept=application/songs-v1+json,application/songs-v1.1+json")
    public List<Song> getAllSongs() { //throws VersionNotSupportedException {
        return songService.getAllSongs();
    }

    @GetMapping("/songs/{id}")
    public ResponseEntity<Song> getSongById(@PathVariable(value = "id") Long songId)
            throws ResourceNotFoundException {
        return ResponseEntity.ok().body(songService.getSongById(songId));
    }

    @GetMapping("/songs/before-year/{year}")
    public ResponseEntity<List<Song>> getSongsBeforeYear(@PathVariable(value = "year") Integer year) {
        return ResponseEntity.ok().body(songService.getSongBeforeYear(year));
    }

    @PostMapping("/songs")
    public Song createSong(@Valid @RequestBody Song song) {
        return songService.createSong(song);
    }

    @PutMapping("/songs/{id}")
    public ResponseEntity<Song> updateSong(@PathVariable(value = "id") Long songId,
                                           @Valid @RequestBody Song songDetails) throws ResourceNotFoundException {
        return ResponseEntity.ok().body(songService.updateSong(songId, songDetails));
    }

    @DeleteMapping("/songs/{id}")
    public Map<String, Boolean> deleteSong(@PathVariable(value = "id") Long songId)
            throws ResourceNotFoundException {
        return songService.deleteSong(songId);
    }

    @GetMapping("/query-songs")
    public List<Song> querySongs(
            @RequestParam(value = "filterOr", required = false) String filterOr,
            @RequestParam(value = "filterAnd", required = false) String filterAnd) {

        Query query = songService.createQuery(filterOr, filterAnd);
        return songService.querySongs(query);
    }

    @GetMapping("/query-songs-pageable")
    public PageResponse<Song> querySongsPageable(
            @RequestParam(value = "filterOr", required = false) String filterOr,
            @RequestParam(value = "filterAnd", required = false) String filterAnd,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "orders", required = false) String orders) {

        Query query = songService.createQuery(filterOr, filterAnd);
        Pageable pageable = songService.createPageable(size, page, orders);
        return songService.querySongsPageable(query, pageable);
    }
}
