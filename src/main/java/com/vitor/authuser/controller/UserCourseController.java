package com.vitor.authuser.controller;

import com.vitor.authuser.client.CourseClient;
import com.vitor.authuser.response.CourseGetResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
@Log4j2
public class UserCourseController {

    private final CourseClient client;

    @GetMapping("/users/{userId}/courses")
    public ResponseEntity<List<CourseGetResponse>> getAllCoursesByUser(@PathVariable(value = "userId") UUID userId) {

        return ResponseEntity.status(HttpStatus.OK).body(client.getAllCoursesByUser(userId));
    }
}
