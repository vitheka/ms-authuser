package com.vitor.authuser.controller;

import com.vitor.authuser.commons.UserUtils;
import com.vitor.authuser.config.IntegrationTestContainers;
import com.vitor.authuser.response.AuthUserPostResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthenticationControllerIT extends IntegrationTestContainers {

    private static final String URL = "/v1/auth";

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private UserUtils userUtils;

    @Test
    @DisplayName("save() create a user")
    void save_CreatesUser_WhenSuccessful() {

        var userToSave = userUtils.newUserSaved();
        var response = testRestTemplate.exchange(URL,HttpMethod.POST,new HttpEntity<>(userToSave),AuthUserPostResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull().hasNoNullFieldsOrProperties();
    }

}