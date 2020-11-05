package com.igar15.rest_course.repository;

import com.igar15.rest_course.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByEmail(String email);

    UserEntity findByUserId(String userId);

    UserEntity findByEmailVerificationToken(String token);

    @Query(value = "select * from Users u where u.email_verification_status = 'true'",
            countQuery = "select count(*) from Users u where u.email_verification_status = 'true'",
            nativeQuery = true)
    Page<UserEntity> findAllUsersWithConfirmedEmailAddress(Pageable pageable);

    @Query(value = "select * from Users u where u.first_name = ?1", nativeQuery = true)
    List<UserEntity> findUserByFirstName(String firstName);

    @Query(value = "select * from Users u where u.last_name =:lastName", nativeQuery = true)
    List<UserEntity> findUserByLastName(@Param("lastName") String lastName);

    @Query(value = "select * from Users u where u.first_name like %:keyword% or u.last_name like %:keyword%", nativeQuery = true)
    List<UserEntity> findUserByKeyword(@Param("keyword") String keyword);

    @Query(value = "select u.first_name, u.last_name from Users u where u.first_name like %:keyword% or u.last_name like %:keyword%", nativeQuery = true)
    List<Object[]> findUserFirstNameAndLastNameByKeyword(@Param("keyword") String keyword);

    @Transactional
    @Modifying
    @Query(value = "update Users u set u.email_verification_status =:emailVerificationStatus where u.user_id =:userId", nativeQuery = true)
    void updateUserEmailVerificationStatus(@Param("emailVerificationStatus") boolean emailVerificationStatus, @Param("userId") String userId);

    @Query("select u from UserEntity u where u.userId =:userId")
    UserEntity findUserEntityByUserId(@Param("userId") String userId);

    @Query("select u.firstName, u.lastName from UserEntity u where u.userId =:userId")
    List<Object[]> findUserEntityFullNameById(@Param("userId") String userId);

    @Transactional
    @Modifying
    @Query("update UserEntity u set u.emailVerificationStatus =:emailVerificationStatus where u.userId =:userId")
    void updateUserEntityEmailVerificationStatus(@Param("emailVerificationStatus") boolean emailVerificationStatus, @Param("userId") String userId);

}
