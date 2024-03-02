package com.vitor.authuser.client;

import com.vitor.authuser.response.CourseGetResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@Log4j2
@Component
@RequiredArgsConstructor
public class CourseClient {

    private final RestTemplate restTemplate;

    @Value("${ead.api.url.course}")
    private String uri;

    @Value("${ead.api.endpoint}")
    private String endpoint;

    public List<CourseGetResponse> getAllCoursesByUser(UUID userId) {

        var uriFormated = UriComponentsBuilder.fromUriString(uri + endpoint)
                .queryParam("userId", userId)
                .toUriString();
        var responseType = new ParameterizedTypeReference<List<CourseGetResponse>>() {};

        var response = restTemplate.exchange(uriFormated, HttpMethod.GET, null, responseType);

        return response.getBody();
    }
}
