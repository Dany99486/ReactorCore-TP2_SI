package org.example.repository;


import org.example.model.Media;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MediaRepository extends JpaRepository<Media, Long> {
    // Adicione consultas personalizadas aqui, se necessário.
}
