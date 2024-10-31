package org.example.repository;

import org.example.model.UserMedia;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserMediaRepository extends ReactiveCrudRepository<UserMedia, Long> {
    Mono<Boolean> existsByUserId(Long userId);
    Mono<Boolean> existsByMediaId(Long mediaId);
    @Query("SELECT media_id FROM user_media WHERE user_id = :userId")
    Flux<Long> findMediaIdsByUserId(Long userId);
    @Query("SELECT user_id FROM user_media WHERE media_id = :mediaId")
    Flux<Long> findUserIdsByMediaId(Long mediaId);

}
