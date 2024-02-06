package com.agap.service;

import com.agap.exception.ResourceNotFoundException;
import com.agap.model.Artist;
import com.agap.model.FilterCondition;
import com.agap.model.PageResponse;
import com.agap.model.Song;
import com.agap.repository.SongSearchRepository;
import com.agap.repository.SongsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Query;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SongServiceTest {

    @Mock
    private SongsRepository songsRepository;

    @Mock
    private SongSearchRepository songSearchRepository;

    @Mock
    private FilterBuilderService filterBuilderService;

    @Mock
    private ArtistService artistService;

    @Mock
    private SequenceGeneratorService sequenceGeneratorService;

    @InjectMocks
    private SongService songService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllSongs() {
        List<Song> songList = new ArrayList<>();
        when(songsRepository.findAll()).thenReturn(songList);

        List<Song> result = songService.getAllSongs();

        assertNotNull(result);
        assertEquals(songList, result);

        verify(songsRepository, times(1)).findAll();
    }

    @Test
    void getSongById() throws ResourceNotFoundException {
        Long songId = 1L;
        Song mockSong = new Song();
        when(songsRepository.findById(songId)).thenReturn(Optional.of(mockSong));

        Song result = songService.getSongById(songId);

        assertNotNull(result);
        assertEquals(mockSong, result);

        verify(songsRepository, times(1)).findById(songId);
    }

    @Test
    void getSongById_ResourceNotFoundException() {
        Long songId = 1L;
        when(songsRepository.findById(songId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> songService.getSongById(songId));

        verify(songsRepository, times(1)).findById(songId);
    }

    @Test
    void createSong() {
        Song song = new Song();
        Artist artist = new Artist();
        when(artistService.createBand(any(Artist.class))).thenReturn(artist);
        when(sequenceGeneratorService.generateSequence(any())).thenReturn(1L);
        when(songsRepository.save(song)).thenReturn(song);

        Song result = songService.createSong(song);

        assertNotNull(result);
        assertEquals(song, result);

        verify(artistService, times(1)).createBand(any(Artist.class));
        verify(sequenceGeneratorService, times(1)).generateSequence(any());
        verify(songsRepository, times(1)).save(song);
    }

    @Test
    void updateSong() throws ResourceNotFoundException {
        Long songId = 1L;
        Song existingSong = new Song();
        Song songDetails = new Song();
        when(songsRepository.findById(songId)).thenReturn(Optional.of(existingSong));
        when(songsRepository.save(existingSong)).thenReturn(existingSong);

        Song result = songService.updateSong(songId, songDetails);

        assertNotNull(result);
        assertEquals(existingSong, result);

        verify(songsRepository, times(1)).findById(songId);
        verify(songsRepository, times(1)).save(existingSong);
    }

    @Test
    void updateSong_ResourceNotFoundException() {
        Long songId = 1L;
        Song songDetails = new Song();
        when(songsRepository.findById(songId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> songService.updateSong(songId, songDetails));

        verify(songsRepository, times(1)).findById(songId);
        verify(songsRepository, times(0)).save(any());
    }

    @Test
    void deleteSong() throws ResourceNotFoundException {
        Long songId = 1L;
        Song existingSong = new Song();
        when(songsRepository.findById(songId)).thenReturn(Optional.of(existingSong));

        songService.deleteSong(songId);

        verify(songsRepository, times(1)).findById(songId);
        verify(songsRepository, times(1)).delete(existingSong);
    }

    @Test
    void deleteSong_ResourceNotFoundException() {
        Long songId = 1L;
        when(songsRepository.findById(songId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> songService.deleteSong(songId));

        verify(songsRepository, times(1)).findById(songId);
        verify(songsRepository, times(0)).delete(any());
    }

    @Test
    void querySongs() {
        Query mockQuery = new Query();
        List<Song> mockSongs = new ArrayList<>();
        when(songSearchRepository.findAll(mockQuery)).thenReturn(mockSongs);

        List<Song> result = songService.querySongs(mockQuery);

        assertNotNull(result);
        assertEquals(mockSongs, result);

        verify(songSearchRepository, times(1)).findAll(mockQuery);
    }

    @Test
    void querySongsPageable() {
        Query mockQuery = new Query();
        Pageable pageable = songService.createPageable(0, 10, "");
        Page<Song> mockPage = mock(Page.class);
        when(songSearchRepository.findAll(mockQuery, pageable)).thenReturn(mockPage);

        PageResponse<Song> result = songService.querySongsPageable(mockQuery, pageable);

        assertNotNull(result);
        verify(songSearchRepository, times(1)).findAll(mockQuery, pageable);
    }

    @Test
    void createPageable() {
        int size = 10;
        int page = 1;
        String orders = "name,asc";
        PageRequest expectedPageable = PageRequest.of(page, size, Sort.by("name").ascending());

        when(filterBuilderService.getPageable(size, page, orders)).thenReturn(expectedPageable);

        Pageable result = songService.createPageable(size, page, orders);

        assertNotNull(result);
        assertEquals(expectedPageable, result);

        verify(filterBuilderService, times(1)).getPageable(size, page, orders);
    }

    @Test
    void createQuery() {
        String filterOr = "name|eq|John";
        String filterAnd = "year|eq|2000";
        List<FilterCondition> mockAndConditions = new ArrayList<>();
        List<FilterCondition> mockOrConditions = new ArrayList<>();

        when(filterBuilderService.createFilterCondition(filterAnd)).thenReturn(mockAndConditions);
        when(filterBuilderService.createFilterCondition(filterOr)).thenReturn(mockOrConditions);

        Query result = songService.createQuery(filterOr, filterAnd);

        assertNotNull(result);
        verify(filterBuilderService, times(1)).createFilterCondition(filterAnd);
        verify(filterBuilderService, times(1)).createFilterCondition(filterOr);
    }

}