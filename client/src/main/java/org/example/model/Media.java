package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Media {

    private Long id; // Identificador da relação

    private Set<Long> userIds;

    private String title;
    private LocalDate releaseDate; // Data de lançamento
    private double averageRating;    // Classificação média entre 0 e 10
    private String type; 
}
