package com.igar15.rest_course.service;

import com.igar15.rest_course.shared.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    UserDto createUser(UserDto user);
}
