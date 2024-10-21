package org.example.controller;

import org.example.model.UserMedia;
import org.example.service.UserMediaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/user-media")
public class UserMediaController {

    private static final Logger logger = LoggerFactory.getLogger(MediaController.class);

    private final UserMediaService userMediaService;

    public UserMediaController(UserMediaService userMediaService) {
        this.userMediaService = userMediaService;
    }

    // Endpoint para associar um user a uma media
    @PostMapping("/add")
    public Mono<ResponseEntity<Object>> createUserMediaRelation(@Valid @RequestBody UserMedia userMedia) {
        return userMediaService.addUserToMedia(userMedia)
                .map(createdUserMedia -> {
                    logger.info("Successfully created user-media relation: {}", createdUserMedia);
                    return ResponseEntity.ok(createdUserMedia);
                })
                .doOnError(error -> logger.error("Failed to create user-media relation: {}", userMedia, error));
    }

    // Endpoint para remover a associação entre um user e uma media
    @DeleteMapping("/remove")
    public Mono<Void> removeUserFromMedia(@RequestParam Long userId, @RequestParam Long mediaId) {
        return userMediaService.removeUserFromMedia(userId, mediaId)
                .doOnSuccess(unused -> logger.info("Successfully removed user-media relation"))
                .doOnError(error -> logger.error("Failed to remove user-media relation", error));

    }
    @GetMapping("/user/{userId}")
    public Flux<Long> getMediaIdsByUserId(@PathVariable Long userId) {
        return userMediaService.getMediaIdsByUserId(userId)
                .doOnComplete(() -> logger.info("Successfully fetched media IDs by user ID"))
                .doOnError(error -> logger.error("Failed to fetch media IDs by user ID", error));
    }
}
