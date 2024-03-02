package com.vitor.authuser.service.impl;

import com.vitor.authuser.commons.UserUtils;
import com.vitor.authuser.domain.User;
import com.vitor.authuser.exception.EmailAlreadyExistsException;
import com.vitor.authuser.exception.NotFoundException;
import com.vitor.authuser.exception.UsernameAlreadyExistsException;
import com.vitor.authuser.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceImplTest {

    public static final String vUUID = "2a7e162d-3394-4082-b42d-92d4298a6ae1";
    @InjectMocks
    private UserServiceImpl service;

    @InjectMocks
    private UserUtils userUtils;

    @Mock
    private UserRepository repository;

    private List<User> usersList;

    @BeforeEach
    void init() {
        usersList = userUtils.newListUsers();
    }

    @Test
    @DisplayName("save() create a user")
    @Order(1)
    void save_CreatesUser_WhenSuccessful() {

        var userToSave = userUtils.newUserToSave();

        when(repository.save(userToSave)).thenReturn(userToSave);
        var user = service.save(userToSave);

        Assertions.assertThat(user).isEqualTo(userToSave).hasNoNullFieldsOrProperties();

    }

    @Test
    @DisplayName("save() Throws EmailAlreadyExistsException")
    @Order(2)
    void save_ThrowsEmailAlreadyExistsException_WhenEmailIsAlreadyTaken() {

        var userToSave = userUtils.newUserToSave();

        when(repository.existsByEmail(userToSave.getEmail())).thenReturn(true);

        Assertions
                .assertThatException()
                .isThrownBy(() -> service.save(userToSave))
                .isInstanceOf(EmailAlreadyExistsException.class);

    }

    @Test
    @DisplayName("save() Throws UsernameAlreadyExistsException")
    @Order(3)
    void save_ThrowsUsernameAlreadyExistsException_WhenUsernameIsAlreadyTaken() {

        var userToSave = userUtils.newUserToSave();

        when(repository.existsByUsername(userToSave.getUsername())).thenReturn(true);

        Assertions
                .assertThatException()
                .isThrownBy(() -> service.save(userToSave))
                .isInstanceOf(UsernameAlreadyExistsException.class);

    }

    @Test
    @DisplayName("findAll() Returns a list of Users")
    @Order(4)
    void findAll_ReturnsAListOfUsers_WhenSuccessful() {

        var courseId = UUID.fromString("7544bf2c-79c5-46ac-b494-4c18a1080887");

        when(repository.findAll(courseId)).thenReturn(this.usersList);

        var user = service.findAll(courseId);

        Assertions.assertThat(user).isNotNull().isNotEmpty();
        Assertions.assertThat(user).hasSameElementsAs(this.usersList);
    }

    @Test
    @DisplayName("findById() Return a User")
    @Order(5)
    void findById_ReturnAUser_WhenSuccessful() {

        when(repository.findById
                (UUID.fromString(vUUID))).thenReturn(Optional.of(this.usersList.get(0)));

        var user = service.findById(UUID.fromString(vUUID));

        Assertions.assertThat(user).isNotNull();
        Assertions.assertThat(user).isEqualTo(this.usersList.get(0));
    }

    @Test
    @DisplayName("findById() ThrowNotFoundException")
    @Order(6)
    void findById_ThrowNotFoundException_WhenUserNotFound() {

        var id = UUID.randomUUID();

        when(repository.findById(id)).thenReturn(Optional.empty());

        Assertions
                .assertThatException()
                .isThrownBy(() -> service.findById(id))
                .isInstanceOf(NotFoundException.class);

    }

    @Test
    @DisplayName("delete() Removes one user")
    @Order(7)
    void delete_RemoveOneUser_WhenSucessfull() {

        var userToDelete = this.usersList.get(0);

        when(repository.findById(UUID.fromString("d41d8cd9-98f0-0000-0000-8427e0000000")))
                .thenReturn(Optional.of(userToDelete));

        BDDMockito.doNothing().when(repository).delete(userToDelete);

        assertThatNoException().isThrownBy(() -> service.delete(UUID.fromString("d41d8cd9-98f0-0000-0000-8427e0000000")));

    }

    @Test
    @DisplayName("delete() Throws NotFoundException")
    @Order(8)
    void delete_ThrowsNotFoundException_WhenUsernameNotFound() {

        var id = UUID.randomUUID();

        when(repository.findById(id)).thenReturn(Optional.empty());

        Assertions
                .assertThatException()
                .isThrownBy(() -> service.delete(id))
                .isInstanceOf(NotFoundException.class);

    }

    @Test
    @DisplayName("update() Att one user")
    @Order(9)
    void update_AttOneUser_WhenSucessfull() {
        var uuid = UUID.fromString("d41d8cd9-98f0-0000-0000-8427e0000000");
        var userToUpdate = this.usersList.get(0);

        when(repository.findById(uuid)).thenReturn(Optional.of(userToUpdate));

        when(repository.save(userToUpdate)).thenReturn(userToUpdate);

        assertThatNoException().isThrownBy(() -> service.update(userToUpdate));

    }

    @Test
    @DisplayName("delete() Throws NotFoundException")
    @Order(10)
    void update_ThrowsNotFoundException_WhenUsernameNotFound() {

        var uuid = UUID.fromString("d41d8cd9-98f0-0000-0000-8427e0000000");
        var userToUpdate = this.usersList.get(0);

        when(repository.findById(uuid)).thenReturn(Optional.empty());

        Assertions
                .assertThatException()
                .isThrownBy(() -> service.update(userToUpdate))
                .isInstanceOf(NotFoundException.class);

    }

}