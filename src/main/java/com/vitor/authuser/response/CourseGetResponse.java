package com.vitor.authuser.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.vitor.authuser.enums.CourseLevel;
import com.vitor.authuser.enums.CourseStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourseGetResponse {

    private UUID courseId;
    private String name;
    private String description;
    private String imageUrl;
    private CourseStatus courseStatus;
    private UUID userInstructor;
    private CourseLevel courseLevel;
}
