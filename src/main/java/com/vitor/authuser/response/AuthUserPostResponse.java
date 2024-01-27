package com.vitor.authuser.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthUserPostResponse {

    private UUID userId;
    private String username;
    private String email;
    private String userPassword;
    private String fullName;
    private String phoneNumber;
    private String cpf;
    private String imageUrl;
}
