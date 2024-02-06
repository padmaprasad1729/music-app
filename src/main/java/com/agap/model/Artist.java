package com.agap.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "artists")
public class Artist {

    @Transient
    public static final String SEQUENCE_NAME = "artists_sequence";

    @Id
    private long id;

    @NotBlank
    @Size(max = 100)
    private String name;
}
