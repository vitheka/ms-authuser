package com.vitor.authuser.controller;


import com.vitor.authuser.commons.FileUtils;
import com.vitor.authuser.commons.UserUtils;
import com.vitor.authuser.exception.CpfAlreadyExistsException;
import com.vitor.authuser.exception.EmailAlreadyExistsException;
import com.vitor.authuser.exception.NotFoundException;
import com.vitor.authuser.exception.UsernameAlreadyExistsException;
import com.vitor.authuser.mapper.UserMapperImpl;
import com.vitor.authuser.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.*;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import({UserMapperImpl.class, UserUtils.class, FileUtils.class})
class UserControllerTest {

    private static final String URL = "/v1/users";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private FileUtils fileUtils;

    @MockBean
    private UserService userService;

    @Test
    @DisplayName("findAll() Returns a list of Users")
    @Order(1)
    void findAll_ReturnsAListOfUsers_WhenSuccessful() throws Exception {

        var courseId = UUID.fromString("7544bf2c-79c5-46ac-b494-4c18a1080887");
        var response = fileUtils.readResourceFile("user-json/get/get-request-list-user-200.json");
        var userFound = userUtils.newUserToSave();

        when(userService.findAll(courseId)).thenReturn(List.of(userFound));

        mvc.perform(get(URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    @DisplayName("findAll() Returns a empty list of Users")
    @Order(2)
    void findAll_ReturnEmptyList_WhenNoUsersAreFound() throws Exception {
        var courseId = UUID.fromString("7544bf2c-79c5-46ac-b494-4c18a1080887");
        when(userService.findAll(courseId)).thenReturn(Collections.emptyList());

        mvc.perform(get(URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @DisplayName("findById() Return a User")
    @Order(3)
    void findById_ReturnAUser_WhenSuccessful() throws Exception {

        var uuid = UUID.fromString("d41d8cd9-98f0-0000-0000-8427e0000000");
        var response = fileUtils.readResourceFile("user-json/get/get-response-one-user-200.json");
        var userFound = userUtils.newListUsers()
                .stream()
                .filter(user -> user.getUserId().equals(uuid))
                .findFirst()
                .orElse(null);


        when(userService.findById(uuid)).thenReturn(userFound);

        mvc.perform(get(URL + "/{userId}", uuid))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    @DisplayName("findById() ThrowNotFoundException")
    @Order(4)
    void findById_ThrowNotFoundException_WhenUserNotFound() throws Exception {

        var invalidUuid = UUID.randomUUID();
        var response = fileUtils.readResourceFile("user-json/response/user-not-found-response-404.json");

        doThrow(new NotFoundException("User not found"))
                .when(userService).findById(any());


        mvc.perform(get(URL + "/{userId}", invalidUuid))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().json(response));

    }

    @Test
    @DisplayName("delete() Removes one user")
    @Order(5)
    void delete_RemoveOneUser_WhenSucessfull() throws Exception {

        var uuid = UUID.fromString("d41d8cd9-98f0-0000-0000-8427e0000000");

        doNothing().when(userService).delete(any());

        mvc.perform(delete(URL + "/{userId}", uuid))
                .andDo(print())
                .andExpect(status().isNoContent());

    }

    @Test
    @DisplayName("delete() Throws NotFoundException")
    @Order(6)
    void delete_ThrowsNotFoundException_WhenUsernameNotFound() throws Exception {

        var invalidUuid = UUID.randomUUID();
        var response = fileUtils.readResourceFile("/user-json/response/user-not-found-response-404.json");

        doThrow(new NotFoundException("User not found"))
                .when(userService).delete(any());

        mvc.perform(delete(URL + "/" + invalidUuid))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().json(response));

    }

    @Test
    @DisplayName("update() Att one user")
    @Order(7)
    void update_AttOneUser_WhenSucessfull() throws Exception {

        doNothing().when(userService).update(any());

        var request = fileUtils.readResourceFile("user-json/put/put-request-user-200.json");

        mvc.perform(put(URL).content(request).contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

    }

    @Test
    @DisplayName("update() Throw NotFoundException")
    @Order(8)
    void update_ThrowNotFoundException_WhenUserNotFound() throws Exception {

        var request = fileUtils.readResourceFile("user-json/put/put-request-user-200.json");
        var response = fileUtils.readResourceFile("user-json/response/user-not-found-response-404.json");

        doThrow(new NotFoundException("User not found"))
                .when(userService).update(any());


        mvc.perform(put(URL).content(request).contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().json(response));

    }

    @Test
    @DisplayName("update() Throw EmailAlreadyExistsException")
    @Order(9)
    void update_ThrowEmailAlreadyExistsException_WhenUserNotFound() throws Exception {

        var request = fileUtils.readResourceFile("user-json/put/put-request-user-200.json");
        var response = fileUtils.readResourceFile("user-json/response/email-already-taken-response-404.json");

        doThrow(new EmailAlreadyExistsException("Email is already taken!"))
                .when(userService).update(any());

        mvc.perform(put(URL).content(request).contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json(response));

    }

    @Test
    @DisplayName("update() Throw UsernameAlreadyExistsException")
    @Order(10)
    void update_ThrowUsernameAlreadyExistsException_WhenUserNotFound() throws Exception {

        var request = fileUtils.readResourceFile("user-json/put/put-request-user-200.json");
        var response = fileUtils.readResourceFile("user-json/response/username-already-taken-response-404.json");

        doThrow(new UsernameAlreadyExistsException("Username is already in taken!"))
                .when(userService).update(any());

        mvc.perform(put(URL).content(request).contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json(response));

    }

    @Test
    @DisplayName("update() Throw UsernameAlreadyExistsException")
    @Order(10)
    void update_ThrowCpfAlreadyExistsException_WhenUserNotFound() throws Exception {

        var request = fileUtils.readResourceFile("user-json/put/put-request-user-200.json");
        var response = fileUtils.readResourceFile("user-json/response/cpf-already-taken-response-404.json");

        doThrow(new CpfAlreadyExistsException("cpf is already in taken!"))
                .when(userService).update(any());

        mvc.perform(put(URL).content(request).contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json(response));

    }

    @ParameterizedTest
    @MethodSource("putUserBadRequestSource")
    @DisplayName("save() returns bad request when fields are invalid")
    @Order(11)
    void update_ReturnsBadRequest_WhenFieldsAreInvalid(String fileName, List<String> errors) throws Exception {

        var request = fileUtils.readResourceFile("user-json/put/%s".formatted(fileName));

        var mvcResult = mvc.perform(MockMvcRequestBuilders
                        .put(URL)
                        .content(request)
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
        var resolvedException = mvcResult.getResolvedException();

        Assertions.assertThat(resolvedException).isNotNull();

        Assertions.assertThat(resolvedException.getMessage()).contains(errors);
    }

    private static Stream<Arguments> putUserBadRequestSource() {
        var errorsMap = allErrorsMap();
        var allErrors = new ArrayList<>(errorsMap.values());

        return Stream.of(
                Arguments.of("put-request-user-blank-fields-400.json", allErrors),
                Arguments.of("put-request-user-empty-fields-400.json", allErrors)
        );
    }

    private static Map<String, String> allErrorsMap() {


        var email = "The field 'email' is required";
        var password = "The field 'userPassword' is required";
        var fullName = "The field 'fullName' is required";
        var phoneNumber = "The field 'phoneNumber' is required";
        var cpf = "The field 'cpf' is required";

        return Map.of(
                "email", email,
                "userPassword", password,
                "fullName", fullName,
                "phoneNumber", phoneNumber,
                "cpf", cpf
        );
    }
}

