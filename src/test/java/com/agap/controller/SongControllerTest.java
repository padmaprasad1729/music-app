package com.agap.controller;

import com.agap.exception.ResourceNotFoundException;
import com.agap.model.PageResponse;
import com.agap.model.Song;
import com.agap.service.SongService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SongControllerTest {

    @Mock
    private SongService songService;

    @InjectMocks
    private SongController songController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllSongs() {
        List<Song> mockSongs = Arrays.asList(new Song(), new Song());
        when(songService.getAllSongs()).thenReturn(mockSongs);

        List<Song> result = songController.getAllSongs();

        assertNotNull(result);
        assertEquals(mockSongs, result);

        verify(songService, times(1)).getAllSongs();
    }

    @Test
    void getSongById() throws ResourceNotFoundException {
        Long songId = 1L;
        Song mockSong = new Song();
        when(songService.getSongById(songId)).thenReturn(mockSong);

        ResponseEntity<Song> result = songController.getSongById(songId);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(mockSong, result.getBody());

        verify(songService, times(1)).getSongById(songId);
    }

    @Test
    void getSongsBeforeYear() {
        int year = 2000;
        List<Song> mockSongs = Arrays.asList(new Song(), new Song());
        when(songService.getSongBeforeYear(year)).thenReturn(mockSongs);

        ResponseEntity<List<Song>> result = songController.getSongsBeforeYear(year);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(mockSongs, result.getBody());

        verify(songService, times(1)).getSongBeforeYear(year);
    }

    @Test
    void createSong() {
        Song song = new Song();
        when(songService.createSong(song)).thenReturn(song);

        Song result = songController.createSong(song);

        assertNotNull(result);
        assertEquals(song, result);

        verify(songService, times(1)).createSong(song);
    }

    @Test
    void updateSong() throws ResourceNotFoundException {
        Long songId = 1L;
        Song songDetails = new Song();
        Song updatedSong = new Song();
        when(songService.updateSong(songId, songDetails)).thenReturn(updatedSong);

        ResponseEntity<Song> result = songController.updateSong(songId, songDetails);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(updatedSong, result.getBody());

        verify(songService, times(1)).updateSong(songId, songDetails);
    }

    @Test
    void deleteSong() throws ResourceNotFoundException {
        Long songId = 1L;
        Map<String, Boolean> deleteResponse = new HashMap<>();
        deleteResponse.put("deleted", true);
        when(songService.deleteSong(songId)).thenReturn(deleteResponse);

        Map<String, Boolean> result = songController.deleteSong(songId);

        assertNotNull(result);
        assertTrue(result.get("deleted"));

        verify(songService, times(1)).deleteSong(songId);
    }

    @Test
    void querySongs() {
        String filterOr = "field1|eq|value1&field2|gt|123";
        Query query = new Query();
        List<Song> mockSongs = Arrays.asList(new Song(), new Song());
        when(songService.createQuery(filterOr, null)).thenReturn(query);
        when(songService.querySongs(query)).thenReturn(mockSongs);

        List<Song> result = songController.querySongs(filterOr, null);

        assertNotNull(result);
        assertEquals(mockSongs, result);

        verify(songService, times(1)).createQuery(filterOr, null);
        verify(songService, times(1)).querySongs(query);
    }

    @Test
    void querySongsPageable() {
        String filterOr = "field1|eq|value1&field2|gt|123";
        String orders = "fieldName|ASC";
        int size = 10;
        int page = 1;
        Query query = new Query();
        PageResponse<Song> pageResponse = new PageResponse<>();
        when(songService.createQuery(filterOr, null)).thenReturn(query);
        when(songService.createPageable(size, page, orders)).thenReturn(mock(Pageable.class));
        when(songService.querySongsPageable(any(), any())).thenReturn(pageResponse);

        PageResponse<Song> result = songController.querySongsPageable(filterOr, null, size, page, orders);

        assertNotNull(result);
        assertEquals(pageResponse, result);

        verify(songService, times(1)).createQuery(filterOr, null);
        verify(songService, times(1)).createPageable(size, page, orders);
        verify(songService, times(1)).querySongsPageable(any(), any());
    }
}