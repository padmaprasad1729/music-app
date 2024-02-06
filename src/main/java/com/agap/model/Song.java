package com.agap.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "songs")
public class Song {

    @Transient
    public static final String SEQUENCE_NAME = "songs_sequence";

    @Id
    private long id;

    @NotBlank
    @Size(max = 100)
    @Indexed(unique = true)
    private String name;

    @Min(1000)
    @Max(4000)
    @Indexed(unique = true)
    private int year;

    @Size(max = 100)
    private String artist;

    @Size(max = 100)
    private String shortname;

    @NotBlank
    @Size(max = 100)
    @Indexed(unique = true)
    private String genre;

    @Size(max = 100)
    private String album;

}
