package com.igar15.rest_course.repository;

import com.igar15.rest_course.entity.PasswordResetTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetTokenEntity, Long> {

    PasswordResetTokenEntity findByToken(String token);
}
