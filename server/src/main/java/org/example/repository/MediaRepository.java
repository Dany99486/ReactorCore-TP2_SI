package org.example.repository;

import org.example.model.Media;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaRepository extends ReactiveCrudRepository<Media, Long> {
    // Adicione métodos personalizados se necessário
}
