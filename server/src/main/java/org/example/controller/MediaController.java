package org.example.controller;

import org.example.model.Media;
import org.example.service.MediaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/media")
public class MediaController {

    private static final Logger logger = LoggerFactory.getLogger(MediaController.class);

    @Autowired
    private MediaService mediaService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Media> getAllMedia() {
        return mediaService.getAllMedia()
                .doOnComplete(() -> logger.info("Successfully fetched all media"))
                .doOnError(error -> logger.error("Failed to fetch all media", error));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Media>> createMedia(@Valid @RequestBody Media media) {
        return mediaService.createMedia(media)
                .map(createdMedia -> {
                    logger.info("Successfully created media with id: {}", createdMedia.getId());
                    return ResponseEntity.status(HttpStatus.CREATED).body(createdMedia);
                })
                .doOnError(error -> logger.error("Failed to create media: {}", media, error));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Media>> getMediaById(@PathVariable Long id) {
        return mediaService.getMediaById(id)
                .map(media -> {
                    logger.info("Successfully fetched media with id: {}", id);
                    return ResponseEntity.ok(media);
                })
                .switchIfEmpty(Mono.defer(() -> {
                    logger.warn("Media with id: {} not found", id);
                    return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
                }))
                .doOnError(error -> logger.error("Failed to fetch media with id: {}", id, error));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Media>> updateMedia(@PathVariable Long id, @Valid @RequestBody Media media) {
        return mediaService.getMediaById(id)
                .flatMap(existingMedia -> mediaService.updateMedia(id, media)
                        .map(updatedMedia -> {
                            logger.info("Successfully updated media with id: {}", id);
                            return ResponseEntity.ok(updatedMedia);
                        }))
                .switchIfEmpty(Mono.defer(() -> {
                    logger.warn("Media with id: {} not found", id);
                    return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
                }))
                .doOnError(error -> logger.error("Failed to update media with id: {}", id, error));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<String>> deleteMedia(@PathVariable Long id) {
        return mediaService.getMediaById(id)
                .flatMap(existingMedia -> mediaService.deleteMedia(id)
                        .then(Mono.defer(() -> {
                            logger.info("Successfully deleted media with id: {}", id);
                            return Mono.just(ResponseEntity.ok("Media deleted successfully"));
                        }))
                        .onErrorResume(error -> {
                            logger.error("Failed to delete media with id: {}", id, error);
                            return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(error.getMessage()));
                        }))
                .switchIfEmpty(Mono.defer(() -> {
                    logger.warn("Media with id: {} not found", id);
                    return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Media not found"));
                }))
                .doOnError(error -> logger.error("Failed to delete media with id: {}", id));
    }



}
