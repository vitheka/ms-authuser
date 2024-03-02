package com.vitor.authuser.service;

import com.vitor.authuser.domain.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User save(User user);

    List<User> findAll(UUID courseId);

    User findById(UUID id);

    void delete(UUID userId);

    void update(User userToUpdate);

    void updateEmail(UUID userId, String email);
}
