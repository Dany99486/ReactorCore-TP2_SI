package org.example.service;

import org.example.model.UserMedia;
import org.example.repository.UserMediaRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserMediaService {

    private final UserMediaRepository userMediaRepository;

    public UserMediaService(UserMediaRepository userMediaRepository) {
        this.userMediaRepository = userMediaRepository;
    }

    // Criar relação entre User e Media
    public Mono<UserMedia> addUserToMedia(UserMedia userMedia) {
        return userMediaRepository.save(userMedia);
    }

    // Remover relação entre User e Media
    public Mono<Void> removeUserFromMedia(Long userId, Long mediaId) {
        return userMediaRepository.findByUserIdAndMediaId(userId, mediaId)
                .flatMap(userMediaRepository::delete);
    }
}

