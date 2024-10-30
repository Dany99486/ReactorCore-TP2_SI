package org.example.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Table("media") // Nome da tabela que mapeia a relação
public class Media {
    @Id
    private Long id; // Identificador da relação

    private Set<Long> userIds;

    @NotBlank(message = "Title is mandatory")
    private String title;

    @NotNull(message = "Release date is mandatory")
    private LocalDate releaseDate; // Data de lançamento

    @NotNull(message = "Average rating is mandatory")
    @Min(value = 0, message = "Average rating must be between 0 and 10")
    @Max(value = 10, message = "Average rating must be between 0 and 10")
    private double averageRating;    // Classificação média entre 0 e 10

    @NotBlank(message = "Type is mandatory")
    
    @NotBlank(message = "Type is mandatory")
    @Pattern(regexp = "Movie|TV Show", message = "Type must be either 'Movie' or 'TV Show'")
    private String type; 
}
