package com.vitor.authuser.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthUserPostRequest {

    @NotBlank
    @Size(min = 4, max = 50)
    private String username;
    @NotBlank(message = "The field 'email' is required")
    private String email;
    @NotBlank(message = "The field 'userPassword' is required")
    @Size(min = 6, max = 50, message = "tamanho deve ser entre 6 e 50")
    private String userPassword;
    @NotBlank(message = "The field 'fullName' is required")
    @Size(max = 150)
    private String fullName;
    @NotBlank(message = "The field 'phoneNumber' is required")
    private String phoneNumber;
    @NotBlank(message = "The field 'cpf' is required")
    private String cpf;
    private String imageUrl;
}
