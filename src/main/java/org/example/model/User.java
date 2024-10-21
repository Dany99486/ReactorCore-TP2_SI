package org.example.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("users") // Nome da tabela no banco de dados
public class User {
    @Id
    private Long id; // Identificador do usuário
    private String name; // Nome
    private Integer age; // Idade
    private String gender; // Gênero
}
