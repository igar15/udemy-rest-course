package com.igar15.rest_course.service.impl;

import com.igar15.rest_course.entity.UserEntity;
import com.igar15.rest_course.repository.UserRepository;
import com.igar15.rest_course.shared.Utils;
import com.igar15.rest_course.shared.dto.AddressDto;
import com.igar15.rest_course.shared.dto.UserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }



    @Test
    void getUser() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(100);
        userEntity.setFirstName("Vasya");
        userEntity.setUserId("sdfsdfsdf");
        userEntity.setEncryptedPassword("sdfsdfsdf");

        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(userEntity);

        UserDto userDto = userService.getUser("test@test.ru");
        Assertions.assertNotNull(userDto);
        Assertions.assertEquals("Vasya", userDto.getFirstName());
    }

    @Test
    void getUserThrowUsernameNotFoundException() {
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(null);

        Assertions.assertThrows(UsernameNotFoundException.class, () -> userService.getUser("test@test.ru"));
    }

}