package com.vitor.authuser.commons;

import com.vitor.authuser.domain.User;
import com.vitor.authuser.enums.UserStatus;
import com.vitor.authuser.enums.UserType;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class UserUtils {

    public User newUserToSave() {

        return User.builder()
                .userId(UUID.fromString("d41d8cd9-98f0-0000-0000-8427e0000000"))
                .username("vitor")
                .email("vitorcoelhoft@gmail.com")
                .userPassword("12345678")
                .fullName("vitor coelho")
                .userStatus(UserStatus.ACTIVE)
                .userType(UserType.STUDENT)
                .phoneNumber("48998377100")
                .cpf("19216386982")
                .imageUrl("")
                .creationDate(LocalDateTime.now())
                .lastUpdateDate(LocalDateTime.now())
                .build();
    }

    public User newUserSaved() {
        return User.builder()
                .username("vitor")
                .email("vitorcoelhoft@gmail.com")
                .userPassword("12345678")
                .fullName("vitor coelho")
                .phoneNumber("99998377100")
                .cpf("19216386982")
                .imageUrl("teste.com.br")
                .build();
    }

    public List<User> newListUsers() {

        var user01 = User.builder()
                .userId(UUID.fromString("d41d8cd9-98f0-0000-0000-8427e0000000"))
                .username("vitor")
                .email("vitorcoelhoft@gmail.com")
                .userPassword("12345678")
                .fullName("vitor coelho")
                .userStatus(UserStatus.ACTIVE)
                .userType(UserType.STUDENT)
                .phoneNumber("48998377100")
                .cpf("19216386982")
                .imageUrl("")
                .creationDate(LocalDateTime.now())
                .lastUpdateDate(LocalDateTime.now())
                .build();

        var user02 = User.builder()
                .userId(UUID.fromString("2a7e162d-3394-4082-b42d-92d4298a6a12"))
                .username("vitzt")
                .email("vitorcoelhozt@gmail.com")
                .userPassword("12345678")
                .fullName("vitor coelho")
                .userStatus(UserStatus.ACTIVE)
                .userType(UserType.STUDENT)
                .phoneNumber("48998377100")
                .cpf("19216386910")
                .imageUrl("")
                .creationDate(LocalDateTime.now())
                .lastUpdateDate(LocalDateTime.now())
                .build();


        return new ArrayList<>(List.of(user01, user02));

    }
}
