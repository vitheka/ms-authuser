package com.vitor.authuser.controller;

import com.vitor.authuser.commons.UserUtils;
import com.vitor.authuser.config.IntegrationTestContainers;
import com.vitor.authuser.repository.UserRepository;
import com.vitor.authuser.response.UserGetResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserControllerIT extends IntegrationTestContainers {

    public static final String URL = "/v1/users";
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private UserRepository repository;

    @Test
    @DisplayName("findAll() Returns a list of Users")
    @Sql(value = "/sql/init_two_users.sql")
    void findAll_ReturnsAListOfUsers_WhenSuccessful() {

        var typeReference = new ParameterizedTypeReference<List<UserGetResponse>>() {
        };

        var response = testRestTemplate.exchange(URL, HttpMethod.GET, null, typeReference);

        assertThat(response.getBody()).isNotEqualTo(new ArrayList<>());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull().doesNotContainNull();

        response.getBody().forEach(userGetResponse -> assertThat(userGetResponse).hasNoNullFieldsOrProperties());
    }

    @Test
    @DisplayName("findAll() Returns a empty list of Users")
    void findAll_ReturnEmptyList_WhenNoUsersAreFound() {
        var typeReference = new ParameterizedTypeReference<List<UserGetResponse>>() {
        };

        var response = testRestTemplate.exchange(URL, HttpMethod.GET, null, typeReference);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull().isEmpty();

    }

    @Test
    @DisplayName("findById() Return a User")
    @Sql(value = "/sql/init_one_user.sql")
    void findById_ReturnAUser_WhenSuccessful() {

        var userId = userUtils.newUserToSave();
        var urlWithId = URL + "/" + userId.getUserId();


        var response = testRestTemplate.exchange(urlWithId, HttpMethod.GET, null, UserGetResponse.class);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("delete() Removes one user")
    @Sql(value = "/sql/init_one_user.sql")
    void delete_RemoveOneUser_WhenSucessfull() {

        var userId = repository.findAll().get(0);
        var urlWithId = URL + "/" + userId.getUserId();

        var response = testRestTemplate.exchange(urlWithId, HttpMethod.DELETE, null, Void.class);

        assertThat(response.getBody()).isNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }

    @Test
    @DisplayName("update() Att one user")
    @Sql(value = "/sql/init_one_user.sql")
    void update_AttOneUser_WhenSucessfull() {

        var userToUpdate = repository.findAll().get(0);
        userToUpdate.setFullName("Monkey D.Luffy");

        var requestEntity = new HttpEntity<>(userToUpdate);

        var response = testRestTemplate.exchange(URL, HttpMethod.PUT, requestEntity, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
