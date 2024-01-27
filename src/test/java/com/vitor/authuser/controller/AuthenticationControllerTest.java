package com.vitor.authuser.controller;

import com.vitor.authuser.commons.FileUtils;
import com.vitor.authuser.commons.UserUtils;
import com.vitor.authuser.exception.CpfAlreadyExistsException;
import com.vitor.authuser.exception.EmailAlreadyExistsException;
import com.vitor.authuser.exception.UsernameAlreadyExistsException;
import com.vitor.authuser.mapper.UserMapperImpl;
import com.vitor.authuser.service.UserService;
import com.vitor.authuser.service.utils.ParseToJson;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthenticationController.class)
@Import({UserMapperImpl.class, UserUtils.class, FileUtils.class, ParseToJson.class})
class AuthenticationControllerTest {

    private static final String URL = "/v1/auth";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private FileUtils fileUtils;

    @Test
    @DisplayName("save() create a user")
    @Order(1)
    void save_CreatesUser_WhenSuccessful() throws Exception {

        var request = fileUtils.readResourceFile("user-json/post/post-request-user-200.json");
        var response = fileUtils.readResourceFile("user-json/post/post-response-user-201.json");
        var userToSave = userUtils.newUserToSave();

        BDDMockito.when(userService.save(any())).thenReturn(userToSave);

        mvc.perform(post(URL)
                        .content(request)
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(response));
    }

    @Test
    @DisplayName("save() Throws EmailAlreadyExistsException")
    @Order(2)
    void save_ThrowsEmailAlreadyExistsException_WhenEmailIsAlreadyTaken() throws Exception {

        var request = fileUtils.readResourceFile("user-json/post/post-request-user-200.json");
        var response = fileUtils.readResourceFile("user-json/response/email-already-taken-response-404.json");

        BDDMockito.when(userService.save(ArgumentMatchers.any()))
                .thenThrow(new EmailAlreadyExistsException("Email is already taken!"));

        mvc.perform(post(URL)
                .content(request)
                .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(response));

    }

    @Test
    @DisplayName("save() Throws UsernameAlreadyExistsException")
    @Order(3)
    void save_ThrowsUsernameAlreadyExistsException_WhenUsernameIsAlreadyTaken() throws Exception {

        var request = fileUtils.readResourceFile("user-json/post/post-request-user-200.json");
        var response = fileUtils.readResourceFile("user-json/response/username-already-taken-response-404.json");

        BDDMockito.when(userService.save(ArgumentMatchers.any()))
                .thenThrow(new UsernameAlreadyExistsException("Username is already in taken!"));

        mvc.perform(post(URL)
                        .content(request)
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(response));

    }

    @Test
    @DisplayName("save() Throws UsernameAlreadyExistsException")
    @Order(4)
    void save_ThrowsCpfAlreadyExistsException_WhenUsernameIsAlreadyTaken() throws Exception {

        var request  = fileUtils.readResourceFile("/user-json/post/post-request-user-200.json");
        var response = fileUtils.readResourceFile("/user-json/response/cpf-already-taken-response-404.json");

        BDDMockito.doThrow(new CpfAlreadyExistsException("cpf is already in taken!"))
                .when(userService).save(ArgumentMatchers.any());

        mvc.perform(post(URL)
                .content(request)
                .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(content().json(response));

    }

    @ParameterizedTest
    @MethodSource("postUserBadRequestSource")
    @DisplayName("save() returns bad request when fields are invalid")
    @Order(5)
    void save_ReturnsBadRequest_WhenFieldsAreInvalid(String fileName, List<String> errors) throws Exception {

        var request = fileUtils.readResourceFile("user-json/post/%s".formatted(fileName));

        var mvcResult = mvc.perform(MockMvcRequestBuilders
                        .post(URL)
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

    private static Stream<Arguments> postUserBadRequestSource() {
        var errorsMap = allErrorsMap();
        var allErrors = new ArrayList<>(errorsMap.values());

        return Stream.of(
                Arguments.of("post-request-user-blank-fields-400.json", allErrors),
                Arguments.of("post-request-user-empty-fields-400.json", allErrors)
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