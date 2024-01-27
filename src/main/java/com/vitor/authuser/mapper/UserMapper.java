package com.vitor.authuser.mapper;

import com.vitor.authuser.domain.User;
import com.vitor.authuser.request.AuthUserPostRequest;
import com.vitor.authuser.request.UserPutRequest;
import com.vitor.authuser.response.AuthUserPostResponse;
import com.vitor.authuser.response.UserGetResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    User toUser(AuthUserPostRequest request);

    AuthUserPostResponse toUserPostResponse(User request);

    List<UserGetResponse> toUserGetResponses(List<User> usersFound);

    UserGetResponse toUserGetResponse(User usersFound);

    User toUser(UserPutRequest request);
}
