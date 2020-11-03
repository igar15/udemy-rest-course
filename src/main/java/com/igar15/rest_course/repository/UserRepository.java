package com.igar15.rest_course.repository;

import com.igar15.rest_course.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByEmail(String email);

    UserEntity findByUserId(String userId);

    UserEntity findByEmailVerificationToken(String token);
}
