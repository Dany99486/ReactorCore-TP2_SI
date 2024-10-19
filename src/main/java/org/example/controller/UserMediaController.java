package org.example.controller;

import org.example.model.User;
import org.example.model.UserMedia;
import org.example.service.UserMediaService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/user-media")
public class UserMediaController {

    private final UserMediaService userMediaService;

    public UserMediaController(UserMediaService userMediaService) {
        this.userMediaService = userMediaService;
    }

    // Endpoint para associar um user a uma media
    @PostMapping("/add")
    public Mono<UserMedia> createUserMediaRelation(@RequestBody UserMedia userMedia) {
        return userMediaService.addUserToMedia(userMedia);
    }

    // Endpoint para remover a associação entre um user e uma media
    @DeleteMapping("/remove")
    public Mono<Void> removeUserFromMedia(@RequestParam Long userId, @RequestParam Long mediaId) {
        return userMediaService.removeUserFromMedia(userId, mediaId);
    }
}
