package org.example.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Data
@Table("media") // Nome da tabela que mapeia a relação
public class Media {
    @Id
    private Long id; // Identificador da relação
    private String title;
    private LocalDate releaseDate; // Data de lançamento
    private double averageRating;    // Classificação média entre 0 e 10
    private String type;              // Tipo: "Movie" ou "TV Show"
}
