package org.example.controller;

import org.example.model.Media;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/media")
public class MediaController {

    @GetMapping
    private Flux<Media> getMedia() {
        return Flux.just(
                new Media("1", "The Shawshank Redemption"),
                new Media("2", "The Godfather")
        );
    }

}
