package com.vitor.authuser.service.impl;

import com.vitor.authuser.domain.User;
import com.vitor.authuser.enums.UserStatus;
import com.vitor.authuser.enums.UserType;
import com.vitor.authuser.exception.CpfAlreadyExistsException;
import com.vitor.authuser.exception.EmailAlreadyExistsException;
import com.vitor.authuser.exception.NotFoundException;
import com.vitor.authuser.exception.UsernameAlreadyExistsException;
import com.vitor.authuser.repository.UserRepository;
import com.vitor.authuser.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Transactional
    @Override
    public User save(User user) {

        if (repository.existsByEmail(user.getEmail())) throw new EmailAlreadyExistsException("Email already taken!");
        if (repository.existsByUsername(user.getUsername())) throw new UsernameAlreadyExistsException("Username already taken!");

        user.setCreationDate(LocalDateTime.now());
        user.setLastUpdateDate(LocalDateTime.now());
        user.setUserStatus(UserStatus.ACTIVE);
        user.setUserType(UserType.STUDENT);

        return repository.save(user);
    }

    @Override
    public List<User> findAll(UUID courseId) {
        return courseId != null ? repository.findAll(courseId) : repository.findAll();
    }


    @Override
    public User findById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("User not found!"));
    }

    @Override
    public void delete(UUID userId) {
        var userToDelete = findById(userId);
        repository.delete(userToDelete);
    }

    @Override
    public void update(User userToUpdate) {

        findById(userToUpdate.getUserId());

        assertUsernameIsUnique(userToUpdate.getUsername(), userToUpdate.getUserId());
        assertEmailIsUnique(userToUpdate.getEmail(), userToUpdate.getUserId());
        assertCpfIsUnique(userToUpdate.getCpf(), userToUpdate.getUserId());

        repository.save(userToUpdate);
    }

    @Override
    public void updateEmail(UUID userId, String email) {
        repository.updateEmail(userId, email);
    }

    private void assertEmailIsUnique(String email, UUID userId) {
        repository.findByEmail(email)
                .ifPresent(userFound -> {
                    if(!userFound.getUserId().equals(userId)) {
                        throw new EmailAlreadyExistsException("Email '%s' already in use".formatted(email));
                    }
                });
    }

    private void assertUsernameIsUnique(String username, UUID userId) {
        repository.findByUsername(username)
                .ifPresent(userFound -> {
                    if(!userFound.getUserId().equals(userId)) {
                        throw new UsernameAlreadyExistsException("Username '%s' already in use".formatted(username));
                    }
                });
    }

    private void assertCpfIsUnique(String cpf, UUID userId) {
        repository.findByCpf(cpf)
                .ifPresent(userFound -> {
                    if(!userFound.getUserId().equals(userId)) {
                        throw new CpfAlreadyExistsException("Cpf '%s' already in use".formatted(cpf));
                    }
                });
    }
}
