package com.vitor.authuser.service.impl;

import com.vitor.authuser.repository.UserCourseRepository;
import com.vitor.authuser.service.UserCourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCourseServiceImpl implements UserCourseService {

    private final UserCourseRepository userCourseRepository;
}
