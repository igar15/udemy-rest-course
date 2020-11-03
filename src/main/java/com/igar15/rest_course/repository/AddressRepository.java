package com.igar15.rest_course.repository;

import com.igar15.rest_course.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<AddressEntity, Long> {

    List<AddressEntity> findAllByUserDetailsUserId(String userId);

    AddressEntity findByAddressId(String addressId);
}
