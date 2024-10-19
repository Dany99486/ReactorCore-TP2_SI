package org.example.service;

import org.example.model.Media;
import org.example.repository.MediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MediaService {

    @Autowired
    private MediaRepository mediaRepository;

    // Método para obter todas as mídias
    public Flux<Media> getAllMedia() {
        return Flux.fromIterable(mediaRepository.findAll());
    }

    // Método para criar uma nova mídia
    public Mono<Media> createMedia(Media media) {
        return Mono.just(mediaRepository.save(media));
    }

    // Método para encontrar mídia por ID
    public Mono<Media> getMediaById(Long id) {
        return Mono.justOrEmpty(mediaRepository.findById(id));
    }

    // Método para excluir uma mídia
    public Mono<Void> deleteMedia(Long id) {
        return Mono.fromRunnable(() -> mediaRepository.deleteById(id));
    }
}
