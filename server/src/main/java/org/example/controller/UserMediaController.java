package org.example.controller;

import org.example.model.Media;
import org.example.service.UserMediaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class UserMediaController {

    private static final Logger logger = LoggerFactory.getLogger(MediaController.class);

    @Autowired
    private UserMediaService userMediaService;

    @PostMapping("media/{mediaId}/users/{userId}")
    public Mono<Media> addUserToMedia(@PathVariable Long mediaId, @PathVariable Long userId) {
        return userMediaService.addUserToMedia(mediaId, userId)
                .doOnSuccess(media -> logger.info("Successfully added user with ID {} to media with ID {}", userId, mediaId))
                .doOnError(error -> logger.error("Failed to add user with ID {} to media with ID {}: {}", userId, mediaId, error.getMessage()));
    }

    @DeleteMapping("media/{mediaId}/users/{userId}")
    public Mono<Media> removeUserFromMedia(@PathVariable Long mediaId, @PathVariable Long userId) {
        return userMediaService.removeUserFromMedia(mediaId, userId)
                .doOnSuccess(media -> logger.info("Successfully removed user with ID {} from media with ID {}", userId, mediaId))
                .doOnError(error -> logger.error("Failed to remove user with ID {} from media with ID {}: {}", userId, mediaId, error.getMessage()));
    }

    @GetMapping("/mediaIds/{userId}")
    public Flux<Long> getMediaIdsByUserId(@PathVariable Long userId) {
        return userMediaService.getMediaIdsByUserId(userId)
                .doOnComplete(() -> logger.info("Successfully fetched media IDs by user ID"))
                .doOnError(error -> logger.error("Failed to fetch media IDs by user ID", error));
    }

    @GetMapping("/userIds/{mediaId}")
    public Flux<Long> getUserIdsByMediaId(@PathVariable Long mediaId) {
        return userMediaService.getUserIdsByMediaId(mediaId)
                .doOnComplete(() -> logger.info("Successfully fetched user IDs by media ID"))
                .doOnError(error -> logger.error("Failed to fetch user IDs by media ID", error));
    }
}