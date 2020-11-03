package com.igar15.rest_course.controller;

import com.igar15.rest_course.model.response.UserRest;
import com.igar15.rest_course.service.impl.UserServiceImpl;
import com.igar15.rest_course.shared.dto.UserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private UserController userController;

     private UserDto userDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        userDto = new UserDto();
        userDto.setFirstName("Vasya");
        userDto.setLastName("Kuralesov");
        userDto.setEmail("test@test.com");
        userDto.setEmailVerificationStatus(false);
        userDto.setEmailVerificationToken(null);
        userDto.setUserId("user_id");
        userDto.setAddresses(new ArrayList<>());
        userDto.setEncryptedPassword("eqwdsafsdfsf");
    }

    @Test
    void getUser() {
        Mockito.when(userService.getUserByUserId(Mockito.anyString())).thenReturn(userDto);

        UserRest userRest = userController.getUser("user_id");

        Assertions.assertNotNull(userRest);
        Assertions.assertEquals("user_id", userRest.getUserId());
        Assertions.assertEquals(userDto.getFirstName(), userRest.getFirstName());
        Assertions.assertEquals(userDto.getLastName(), userRest.getLastName());
    }
}