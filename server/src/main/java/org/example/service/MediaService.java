package org.example.service;

import org.example.model.Media;
import org.example.model.UserMedia;
import org.example.repository.MediaRepository;
import org.example.repository.UserMediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Set;

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
                .flatMap(existingMedia -> {
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

    // Adicionar um usuário a uma mídia
    public Mono<Media> addUserToMedia(Long mediaId, Long userId) {
        return mediaRepository.findById(mediaId)
                .flatMap(media -> {
                    // Adiciona o ID do usuário na lista da mídia
                    Set<Long> userIds = media.getUserIds() != null ? media.getUserIds() : new HashSet<>();
                    userIds.add(userId);
                    media.setUserIds(userIds);

                    // Salva a relação na tabela de junção
                    UserMedia userMedia = new UserMedia();
                    userMedia.setUserId(userId);
                    userMedia.setMediaId(mediaId);

                    return userMediaRepository.save(userMedia)
                            .then(mediaRepository.save(media)); // Salva a mídia atualizada
                });
    }

    // Remover um usuário de uma mídia
    public Mono<Media> removeUserFromMedia(Long mediaId, Long userId) {
        return mediaRepository.findById(mediaId)
                .flatMap(media -> {
                    // Remove o ID do usuário na lista da mídia
                    Set<Long> userIds = media.getUserIds();
                    if (userIds != null) {
                        userIds.remove(userId);
                        media.setUserIds(userIds);
                    }

                    // Remove a relação da tabela de junção
                    return userMediaRepository.findAll()
                            .filter(userMedia -> userMedia.getMediaId().equals(mediaId) && userMedia.getUserId().equals(userId))
                            .flatMap(userMediaRepository::delete)
                            .then(mediaRepository.save(media)); // Salva a mídia atualizada
                });
    }

}
