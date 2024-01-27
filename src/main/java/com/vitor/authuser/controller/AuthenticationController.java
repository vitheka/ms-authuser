package com.vitor.authuser.controller;

import com.vitor.authuser.mapper.UserMapper;
import com.vitor.authuser.request.AuthUserPostRequest;
import com.vitor.authuser.response.AuthUserPostResponse;
import com.vitor.authuser.service.UserService;
import com.vitor.authuser.service.utils.ParseToJson;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
@Log4j2
public class AuthenticationController {

    private final UserService userService;
    private final ParseToJson json;
    private final UserMapper mapper;

    @PostMapping
    public ResponseEntity<AuthUserPostResponse> registerUser(@RequestBody @Valid AuthUserPostRequest request) {

        log.info("Request body received to register: {}", json.objToJson(request));

        var user = mapper.toUser(request);

        user = userService.save(user);

        var response = mapper.toUserPostResponse(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
