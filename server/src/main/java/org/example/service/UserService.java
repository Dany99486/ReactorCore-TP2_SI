package org.example.service;

import org.example.model.User;
import org.example.repository.UserMediaRepository;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMediaRepository userMediaRepository;

    // Método para obter todos os usuários
    public Flux<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Método para criar um novo usuário
    public Mono<User> createUser(User user) {
        return userRepository.save(user);
    }

    // Método para encontrar usuário por ID
    public Mono<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // Método para atualizar um usuário
    public Mono<User> updateUser(Long id, User user) {
        return userRepository.findById(id)
                .flatMap(existingUser -> userRepository.save(user))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")));
    }

    // Método para excluir um usuário
    public Mono<Void> deleteUser(Long id) {
        return userMediaRepository.existsByUserId(id)  // Verifica se o usuário está em uma relação
            .flatMap(exists -> {
                if (exists){
                    return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "User cannot be deleted because it is linked to media"));
                }else{
                    return userRepository.deleteById(id); // Chama o método delete apenas se o usuário não estiver vinculado a nenhuma mídia
                }
                });    
            }
}
