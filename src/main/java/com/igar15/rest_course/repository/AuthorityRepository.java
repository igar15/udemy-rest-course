package com.igar15.rest_course.repository;

import com.igar15.rest_course.entity.AuthorityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends JpaRepository<AuthorityEntity, Long> {

    AuthorityEntity findByName(String name);
}
