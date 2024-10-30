package org.example.service;

import org.example.model.Media;
import org.example.model.User;
import org.example.model.UserMedia;
import org.example.repository.MediaRepository;
import org.example.repository.UserMediaRepository;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserMediaService {

    @Autowired
    private MediaRepository mediaRepository;
    @Autowired
    private UserMediaRepository userMediaRepository;
    @Autowired
    private UserRepository userRepository;

    // Adicionar um usuário a uma mídia
    public Mono<Media> addUserToMedia(Long mediaId, Long userId) {
        return mediaRepository.findById(mediaId)
                .flatMap(media -> {
                    // Atualiza a lista de IDs de usuários na mídia
                    Set<Long> userIds = media.getUserIds() != null ? media.getUserIds() : new HashSet<>();
                    userIds.add(userId);
                    media.setUserIds(userIds);

                    // Salva a relação na tabela de junção
                    UserMedia userMedia = new UserMedia();
                    userMedia.setUserId(userId);
                    userMedia.setMediaId(mediaId);

                    // Atualiza a lista de IDs de mídias no usuário
                    return userRepository.findById(userId)
                            .flatMap(user -> {
                                Set<Long> mediaIds = user.getMediaIds() != null ? user.getMediaIds() : new HashSet<>();
                                mediaIds.add(mediaId);
                                user.setMediaIds(mediaIds);
                                return userRepository.save(user);
                            })
                            .then(userMediaRepository.save(userMedia))
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

                    // Atualiza a lista de IDs de mídias no usuário
                    return userRepository.findById(userId)
                            .flatMap(user -> {
                                Set<Long> mediaIds = user.getMediaIds();
                                if (mediaIds != null) {
                                    mediaIds.remove(mediaId);
                                    user.setMediaIds(mediaIds);
                                }
                                return userRepository.save(user);
                            })
                            .then(userMediaRepository.findAll()
                                    .filter(userMedia -> userMedia.getMediaId().equals(mediaId) && userMedia.getUserId().equals(userId))
                                    .flatMap(userMediaRepository::delete)
                                    .then()) // Converte para Mono<Void>
                            .then(mediaRepository.save(media)); // Salva a mídia atualizada
                });
    }
}
