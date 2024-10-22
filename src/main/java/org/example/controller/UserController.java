package org.example.controller;

import org.example.model.User;
import org.example.service.UserService;
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
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(MediaController.class);

    @Autowired
    private UserService userService;

    // Endpoint para obter todos os usuários
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<User> getAllUsers() {
        return userService.getAllUsers()
                .doOnComplete(() -> logger.info("Successfully fetched all users"))
                .doOnError(error -> logger.error("Failed to fetch all users", error));
    }

    // Endpoint para criar um novo usuário
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<User>> createUser(@Valid @RequestBody User user) {
        return userService.createUser(user)
                .map(createdUser -> {
                    logger.info("Successfully created user with id: {}", createdUser.getId());
                    return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
                })
                .doOnError(error -> logger.error("Failed to create user: {}", user, error));
    }

    // Endpoint para encontrar um usuário por ID
    @GetMapping("/{id}")
    public Mono<ResponseEntity<User>> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                    .map(user -> {
                        logger.info("Successfully fetched user with id: {}", id);
                        return ResponseEntity.ok(user);
                    })
                    .switchIfEmpty(Mono.defer(() -> {
                        logger.warn("User with id: {} not found", id);
                        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
                    }))
                .doOnError(error -> logger.error("Failed to fetch user with id: {}", id, error));
    }

    // Endpoint para atualizar um usuário
    @PutMapping("/{id}")
    public Mono<ResponseEntity<User>> updateUser(@PathVariable Long id, @Valid @RequestBody User user) {
        return userService.getUserById(id)
                    .flatMap(existingUser -> userService.updateUser(id, user)
                            .map(updatedUser -> {
                                logger.info("Successfully updated user with id: {}", id);
                                return ResponseEntity.ok(updatedUser);
                            }))
                    .switchIfEmpty(Mono.defer(() -> {
                        logger.warn("User with id: {} not found", id);
                        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
                }))
                .doOnError(error -> logger.error("Failed to update user with id: {}", id, error));
    }

    // Endpoint para excluir um usuário
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<String>> deleteUser(@PathVariable Long id) {
        return userService.getUserById(id)
                    .flatMap(existingUser -> userService.deleteUser(id)
                            .then(Mono.defer(() -> {
                                logger.info("Successfully deleted user with id: {}", id);
                                return Mono.just(ResponseEntity.ok("User deleted successfully"));
                            }))
                            .onErrorResume(error -> {
                                logger.error("Failed to delete user with id: {}", id, error);
                                return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(error.getMessage()));
                            }))
                    .switchIfEmpty(Mono.defer(() -> {
                    logger.warn("User with id: {} not found", id);
                    return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
                }))
                .doOnError(error -> logger.error("Failed to delete user with id: {}", id, error));
    }
}
