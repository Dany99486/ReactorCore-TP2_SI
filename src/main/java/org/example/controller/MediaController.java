package org.example.controller;

import org.example.model.Media;
import org.example.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
    public Mono<Media> getMediaById(@PathVariable Long id) {
        return mediaService.getMediaById(id);
    }

    @PutMapping("/{id}")
    public Mono<Media> updateMedia(@PathVariable Long id, @RequestBody Media media) {
        return mediaService.updateMedia(id, media);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteMedia(@PathVariable Long id) {
        return mediaService.deleteMedia(id);
    }
}
