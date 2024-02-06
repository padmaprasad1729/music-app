package com.agap.controller;

import com.agap.exception.ResourceNotFoundException;
import com.agap.model.Artist;
import com.agap.service.ArtistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArtistControllerTest {

    @Mock
    private ArtistService artistService;

    @InjectMocks
    private ArtistController artistController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllBands() {
        List<Artist> mockArtists = Arrays.asList(new Artist(), new Artist());
        when(artistService.getAllBands()).thenReturn(mockArtists);

        List<Artist> result = artistController.getAllBands();

        assertNotNull(result);
        assertEquals(mockArtists, result);

        verify(artistService, times(1)).getAllBands();
    }

    @Test
    void getBandById() throws ResourceNotFoundException {
        Long artistId = 1L;
        Artist mockArtist = new Artist();
        when(artistService.getArtistById(artistId)).thenReturn(mockArtist);

        ResponseEntity<Artist> result = artistController.getBandById(artistId);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(mockArtist, result.getBody());

        verify(artistService, times(1)).getArtistById(artistId);
    }

    @Test
    void createBand() {
        Artist artist = new Artist();
        when(artistService.createBand(artist)).thenReturn(artist);

        Artist result = artistController.createBand(artist);

        assertNotNull(result);
        assertEquals(artist, result);

        verify(artistService, times(1)).createBand(artist);
    }

    @Test
    void updateBand() throws ResourceNotFoundException {
        Long artistId = 1L;
        Artist artistDetails = new Artist();
        Artist updatedArtist = new Artist();
        when(artistService.updateArtist(artistId, artistDetails)).thenReturn(updatedArtist);

        ResponseEntity<Artist> result = artistController.updateBand(artistId, artistDetails);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(updatedArtist, result.getBody());

        verify(artistService, times(1)).updateArtist(artistId, artistDetails);
    }

    @Test
    void deleteBand() throws ResourceNotFoundException {
        Long artistId = 1L;
        Map<String, Boolean> deleteResponse = new HashMap<>();
        deleteResponse.put("deleted", true);
        when(artistService.deleteArtist(artistId)).thenReturn(deleteResponse);

        Map<String, Boolean> result = artistController.deleteBand(artistId);

        assertNotNull(result);
        assertTrue(result.get("deleted"));

        verify(artistService, times(1)).deleteArtist(artistId);
    }
}