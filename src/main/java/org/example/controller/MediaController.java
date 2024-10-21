package org.example.controller;

import org.example.model.Media;
import org.example.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/media")
public class MediaController {

    @Autowired
    private MediaService mediaService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Media> getAllMedia() {
        return mediaService.getAllMedia();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Media> createMedia(@RequestBody Media media) {
        return mediaService.createMedia(media);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Media>> getMediaById(@PathVariable Long id) {
        return mediaService.getMediaById(id)
                .map(media -> ResponseEntity.ok(media))
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Media>> updateMedia(@PathVariable Long id, @RequestBody Media media) {
        return mediaService.getMediaById(id)
                .flatMap(existingMedia -> mediaService.updateMedia(id, media)
                        .map(updatedMedia -> ResponseEntity.ok(updatedMedia)))
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<String>> deleteMedia(@PathVariable Long id) {
        return mediaService.getMediaById(id)
                .flatMap(media -> mediaService.deleteMedia(id)
                        .then(Mono.just(ResponseEntity.ok("Media deleted successfully"))))
                
                // Tratamento de erro para o caso de a mídia não existir, sendo retornado um status 404
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()));
    }
}
