package com.agap.service;

import com.agap.exception.ResourceNotFoundException;
import com.agap.model.Artist;
import com.agap.repository.ArtistsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArtistServiceTest {

    @Mock
    private ArtistsRepository artistsRepository;

    @Mock
    private SequenceGeneratorService sequenceGeneratorService;

    @InjectMocks
    private ArtistService artistService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllBands() {
        List<Artist> artistList = new ArrayList<>();
        when(artistsRepository.findAll()).thenReturn(artistList);

        List<Artist> result = artistService.getAllBands();

        assertNotNull(result);
        assertEquals(artistList, result);

        verify(artistsRepository, times(1)).findAll();
    }

    @Test
    void getArtistById() throws ResourceNotFoundException {
        Long artistId = 1L;
        Artist mockArtist = new Artist();
        when(artistsRepository.findById(artistId)).thenReturn(Optional.of(mockArtist));

        Artist result = artistService.getArtistById(artistId);

        assertNotNull(result);
        assertEquals(mockArtist, result);

        verify(artistsRepository, times(1)).findById(artistId);
    }

    @Test
    void getArtistById_ResourceNotFoundException() {
        Long artistId = 1L;
        when(artistsRepository.findById(artistId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> artistService.getArtistById(artistId));

        verify(artistsRepository, times(1)).findById(artistId);
    }

    @Test
    void createBand() {
        Artist artist = new Artist();
        when(artistsRepository.getByName(artist.getName())).thenReturn(Optional.empty());
        when(sequenceGeneratorService.generateSequence(any())).thenReturn(1L);
        when(artistsRepository.save(artist)).thenReturn(artist);

        Artist result = artistService.createBand(artist);

        assertNotNull(result);
        assertEquals(artist, result);

        verify(artistsRepository, times(1)).getByName(artist.getName());
        verify(sequenceGeneratorService, times(1)).generateSequence(any());
        verify(artistsRepository, times(1)).save(artist);
    }

    @Test
    void createBand_ArtistAlreadyExists() {
        Artist artist = new Artist();
        when(artistsRepository.getByName(artist.getName())).thenReturn(Optional.of(artist));

        Artist result = artistService.createBand(artist);

        assertNotNull(result);
        assertEquals(artist, result);

        verify(artistsRepository, times(1)).getByName(artist.getName());
        verify(sequenceGeneratorService, times(0)).generateSequence(any());
        verify(artistsRepository, times(0)).save(any());
    }


    @Test
    void updateArtist() throws ResourceNotFoundException {
        Long artistId = 1L;
        Artist existingArtist = new Artist();
        Artist artistDetails = new Artist();
        when(artistsRepository.findById(artistId)).thenReturn(Optional.of(existingArtist));
        when(artistsRepository.save(existingArtist)).thenReturn(existingArtist);

        Artist result = artistService.updateArtist(artistId, artistDetails);

        assertNotNull(result);
        assertEquals(existingArtist, result);

        verify(artistsRepository, times(1)).findById(artistId);
        verify(artistsRepository, times(1)).save(existingArtist);
    }

    @Test
    void updateArtist_ResourceNotFoundException() {
        Long artistId = 1L;
        Artist artistDetails = new Artist();
        when(artistsRepository.findById(artistId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> artistService.updateArtist(artistId, artistDetails));

        verify(artistsRepository, times(1)).findById(artistId);
        verify(artistsRepository, times(0)).save(any());
    }

    @Test
    void deleteArtist() throws ResourceNotFoundException {
        Long artistId = 1L;
        Artist existingArtist = new Artist();
        when(artistsRepository.findById(artistId)).thenReturn(Optional.of(existingArtist));

        artistService.deleteArtist(artistId);

        verify(artistsRepository, times(1)).findById(artistId);
        verify(artistsRepository, times(1)).delete(existingArtist);
    }

    @Test
    void deleteArtist_ResourceNotFoundException() {
        Long artistId = 1L;
        when(artistsRepository.findById(artistId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> artistService.deleteArtist(artistId));

        verify(artistsRepository, times(1)).findById(artistId);
        verify(artistsRepository, times(0)).delete(any());
    }
}