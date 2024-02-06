package com.agap.controller;

import com.agap.exception.ResourceNotFoundException;
import com.agap.model.Artist;
import com.agap.service.ArtistService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class ArtistController {

    @Autowired
    private ArtistService artistService;

    @GetMapping("/bands")
    public List<Artist> getAllBands() {
        return artistService.getAllBands();
    }

    @GetMapping("/bands/{id}")
    public ResponseEntity<Artist> getBandById(@PathVariable(value = "id") Long artistId)
            throws ResourceNotFoundException {
        return ResponseEntity.ok().body(artistService.getArtistById(artistId));
    }

    @PostMapping("/bands")
    public Artist createBand(@Valid @RequestBody Artist artist) {
        return artistService.createBand(artist);
    }

    @PutMapping("/bands/{id}")
    public ResponseEntity<Artist> updateBand(@PathVariable(value = "id") Long artistId,
                                             @Valid @RequestBody Artist artistDetails) throws ResourceNotFoundException {
        return ResponseEntity.ok().body(artistService.updateArtist(artistId, artistDetails));
    }

    @DeleteMapping("/bands/{id}")
    public Map<String, Boolean> deleteBand(@PathVariable(value = "id") Long artistId)
            throws ResourceNotFoundException {
        return artistService.deleteArtist(artistId);
    }
}
