package org.example.repository;

import org.example.model.UserMedia;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserMediaRepository extends ReactiveCrudRepository<UserMedia, Long> {
    Mono<Boolean> existsByUserId(Long userId);
    Mono<Boolean> existsByMediaId(Long mediaId);

}
