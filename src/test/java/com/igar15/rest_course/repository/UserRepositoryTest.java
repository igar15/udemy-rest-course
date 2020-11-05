package com.igar15.rest_course.repository;

import com.igar15.rest_course.entity.UserEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private static boolean recordsCreated = false;

    @BeforeEach
    void setUp() {
        if (!recordsCreated) {
            UserEntity userEntity = new UserEntity();
            userEntity.setFirstName("Vasya");
            userEntity.setLastName("Kur");
            userEntity.setUserId("123asd");
            userEntity.setEncryptedPassword("dsfsdfsf");
            userEntity.setEmail("vasya@test.com");
            userEntity.setEmailVerificationStatus(true);
            userRepository.save(userEntity);

            UserEntity userEntity2 = new UserEntity();
            userEntity2.setFirstName("Vasya2");
            userEntity2.setLastName("Kur");
            userEntity2.setUserId("1234asd");
            userEntity2.setEncryptedPassword("dsfsdfsf");
            userEntity2.setEmail("vasya2@test.com");
            userEntity2.setEmailVerificationStatus(true);
            userRepository.save(userEntity2);

            recordsCreated = true;
        }
    }


    @Test
    void testGetVerifiedUsers() {
        Pageable pageable = PageRequest.of(0, 1);
        Page<UserEntity> users = userRepository.findAllUsersWithConfirmedEmailAddress(pageable);
        Assertions.assertNotNull(users);

        List<UserEntity> content = users.getContent();
        Assertions.assertNotNull(content);
        Assertions.assertEquals(1, content.size());
    }

    @Test
    void findUserByFirstName() {
        String firstName = "Vasya";
        List<UserEntity> users = userRepository.findUserByFirstName(firstName);
        Assertions.assertNotNull(users);
        Assertions.assertEquals(1, users.size());

        UserEntity user = users.get(0);
        Assertions.assertEquals(firstName, user.getFirstName());
    }

    @Test
    void findUserByLastName() {
        String lastName = "Kur";
        List<UserEntity> users = userRepository.findUserByLastName(lastName);
        Assertions.assertNotNull(users);
        Assertions.assertEquals(2, users.size());

        UserEntity user = users.get(0);
        Assertions.assertEquals(lastName, user.getLastName());
    }

    @Test
    void findUserByKeyword() {
        String keyword = "Ku";
        List<UserEntity> users = userRepository.findUserByKeyword(keyword);
        Assertions.assertNotNull(users);
        Assertions.assertEquals(2, users.size());

        UserEntity user = users.get(0);
        Assertions.assertTrue(user.getLastName().contains(keyword) || user.getFirstName().contains(keyword));
    }

    @Test
    void findUserFirstNameAndLastNameByKeyword() {
        String keyword = "Ku";
        List<Object[]> users = userRepository.findUserFirstNameAndLastNameByKeyword(keyword);
        Assertions.assertNotNull(users);
        Assertions.assertEquals(2, users.size());

        Object[] user = users.get(0);
        String userFirstName = (String) user[0];
        String userLastName = (String) user[1];

        Assertions.assertNotNull(userFirstName);
        Assertions.assertNotNull(userLastName);
    }

    @Test
    void updateUserEmailVerificationStatus() {
        boolean emailVerificationStatus = false;
        String userId = "123asd";
        userRepository.updateUserEmailVerificationStatus(emailVerificationStatus, userId);

        UserEntity user = userRepository.findByUserId(userId);
        Assertions.assertEquals(emailVerificationStatus, user.getEmailVerificationStatus());
    }

    @Test
    void findUserEntityByUserId() {
        String userId = "123asd";
        UserEntity userEntity = userRepository.findUserEntityByUserId(userId);
        Assertions.assertNotNull(userEntity);
        Assertions.assertEquals(userId, userEntity.getUserId());
    }

    @Test
    void findUserEntityFullNameById() {
        String userId = "123asd";
        List<Object[]> userFullName = userRepository.findUserEntityFullNameById(userId);
        Assertions.assertNotNull(userFullName);
        Assertions.assertNotNull(userFullName.get(0)[0]);
        Assertions.assertNotNull(userFullName.get(0)[1]);
    }

    @Test
    void updateUserEntityEmailVerificationStatus() {
        boolean emailVerificationStatus = false;
        String userId = "123asd";
        userRepository.updateUserEntityEmailVerificationStatus(emailVerificationStatus, userId);

        UserEntity user = userRepository.findByUserId(userId);
        Assertions.assertEquals(emailVerificationStatus, user.getEmailVerificationStatus());
    }


}