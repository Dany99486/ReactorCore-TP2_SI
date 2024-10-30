package org.example.controller;

import jakarta.validation.Valid;
import org.example.model.Media;
import org.example.model.User;
import org.example.service.UserMediaService;
import org.example.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
        return userMediaService.addUserToMedia(mediaId, userId);
    }

    @DeleteMapping("media/{mediaId}/users/{userId}")
    public Mono<Media> removeUserFromMedia(@PathVariable Long mediaId, @PathVariable Long userId) {
        return userMediaService.removeUserFromMedia(mediaId, userId);
    }
}