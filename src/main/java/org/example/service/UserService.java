package org.example.service;

import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

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
                .flatMap(existingUser -> {
                    existingUser.setName(user.getName());
                    existingUser.setAge(user.getAge());
                    existingUser.setGender(user.getGender());
                    return userRepository.save(existingUser);
                })
                .switchIfEmpty(Mono.error(new RuntimeException("User not found")));
    }

    // Método para excluir um usuário
    public Mono<Void> deleteUser(Long id) {
        return userRepository.deleteById(id);
    }
}
