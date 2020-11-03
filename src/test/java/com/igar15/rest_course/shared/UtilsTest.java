package com.igar15.rest_course.shared;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UtilsTest {

    @Autowired
    private Utils utils;

    @BeforeEach
    void setUp() {
    }

    @Test
    void generateUserId() {
        String userId = utils.generateUserId(30);
        String userId2 = utils.generateUserId(30);

        Assertions.assertNotNull(userId);
        Assertions.assertTrue(userId.length() == 30);
        Assertions.assertTrue(!userId.equalsIgnoreCase(userId2));
    }

    @Test
    void hasTokenExpired() {
        String token = utils.generateEmailVerificationToken("qwerty");

        boolean hasTokenExpired = utils.hasTokenExpired(token);

        Assertions.assertFalse(hasTokenExpired);

    }


}