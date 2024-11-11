package org.example.service;

import org.example.model.Media;
import org.example.repository.MediaRepository;
import org.example.repository.UserMediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MediaService {

    @Autowired
    private MediaRepository mediaRepository;
    @Autowired
    private UserMediaRepository userMediaRepository;

    public Flux<Media> getAllMedia() {
        return mediaRepository.findAll();
    }

    public Mono<Media> createMedia(Media media) {
        return mediaRepository.save(media);
    }

    public Mono<Media> getMediaById(Long id) {
        return mediaRepository.findById(id);
    }

    // Método para atualizar uma mídia
    public Mono<Media> updateMedia(Long id, Media media) {
        return mediaRepository.findById(id)
                .flatMap( existingMedia -> {
                    existingMedia.setTitle(media.getTitle());
                    existingMedia.setReleaseDate(media.getReleaseDate());
                    existingMedia.setAverageRating(media.getAverageRating());
                    existingMedia.setType(media.getType());
                    return mediaRepository.save(existingMedia);
                })
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Media not found")));
    }

    public Mono<Void> deleteMedia(Long id) {
        return userMediaRepository.existsByMediaId(id)
            .flatMap(exists -> {
                if (exists) {
                    return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "Media cannot be deleted because it is linked to user"));
                } else {
                    return mediaRepository.deleteById(id);
                }
            });
    }


}
