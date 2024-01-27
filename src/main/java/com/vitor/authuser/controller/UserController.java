package com.vitor.authuser.controller;

import com.vitor.authuser.mapper.UserMapper;
import com.vitor.authuser.request.UserPutRequest;
import com.vitor.authuser.response.UserGetResponse;
import com.vitor.authuser.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/v1/users")
@RequiredArgsConstructor
@Log4j2
public class UserController {

    private final UserService userService;
    private final UserMapper mapper;

    @GetMapping
    public ResponseEntity<List<UserGetResponse>> getAllUsers() {

        var usersFound = userService.findAll();

        var response = mapper.toUserGetResponses(usersFound);

        return ResponseEntity.ok().body(response);

    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserGetResponse> getOneUser(@PathVariable UUID userId) {

        var usersFound = userService.findById(userId);

        var response = mapper.toUserGetResponse(usersFound);

        return ResponseEntity.ok().body(response);

    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {

        userService.delete(userId);

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Void> updateUser(@RequestBody @Valid UserPutRequest request) {

        var userToUpdate = mapper.toUser(request);

        userService.update(userToUpdate);

        return ResponseEntity.noContent().build();

    }

    //todo test unit and IT
    @PatchMapping("/{userId}")
    public ResponseEntity<Void> updateEmail(@PathVariable UUID userId,
                                            @RequestParam String email) {
        userService.updateEmail(userId, email);

        return ResponseEntity.accepted().build();
    }
}
