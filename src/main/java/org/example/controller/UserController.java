package org.example.controller;

import org.example.model.User;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    // Endpoint para obter todos os usuários
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // Endpoint para criar um novo usuário
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<User> createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    // Endpoint para encontrar um usuário por ID
    @GetMapping("/{id}")
    public Mono<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    // Endpoint para atualizar um usuário
    @PutMapping("/{id}")
    public Mono<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    // Endpoint para excluir um usuário
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<String>> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id)
                .then(Mono.just(ResponseEntity.ok("")))
                .onErrorResume(e -> {
                    // Em caso de erro, retorne uma resposta com o status 400 e a mensagem de erro
                    return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()));
                });
    }
}
