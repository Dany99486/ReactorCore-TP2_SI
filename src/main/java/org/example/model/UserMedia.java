package org.example.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("user_media")
public class UserMedia {

    @Id
    private Long id;  // Essa é a chave primária da tabela de junção (opcional)
    private Long userId;  // Chave estrangeira para a entidade User
    private Long mediaId; // Chave estrangeira para a entidade Media

    // Construtores, getters e setters
}
