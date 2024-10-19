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
        return mediaRepository.findAll(); // Já retorna um Flux<Media>
    }

    // Método para criar uma nova mídia
    public Mono<Media> createMedia(Media media) {
        return mediaRepository.save(media); // Já retorna um Mono<Media>
    }

    // Método para encontrar mídia por ID
    public Mono<Media> getMediaById(Long id) {
        return mediaRepository.findById(id); // Já retorna um Mono<Media>
    }

    // Método para atualizar uma mídia
    public Mono<Media> updateMedia(Long id, Media media) {
        return mediaRepository.findById(id)
                .flatMap(existingMedia -> {
                    existingMedia.setTitle(media.getTitle());
                    existingMedia.setReleaseDate(media.getReleaseDate());
                    existingMedia.setAverageRating(media.getAverageRating());
                    existingMedia.setType(media.getType());
                    return mediaRepository.save(existingMedia); // Salva a mídia atualizada
                })
                .switchIfEmpty(Mono.error(new RuntimeException("Media not found"))); // Lida com o caso em que a mídia não é encontrada
    }

    // Método para excluir uma mídia
    public Mono<Void> deleteMedia(Long id) {
        return mediaRepository.deleteById(id); // Método reativo para deletar
    }
}
