package com.igar15.rest_course.service.impl;

import com.igar15.rest_course.entity.AddressEntity;
import com.igar15.rest_course.entity.UserEntity;
import com.igar15.rest_course.repository.AddressRepository;
import com.igar15.rest_course.repository.UserRepository;
import com.igar15.rest_course.service.AddressService;
import com.igar15.rest_course.shared.dto.AddressDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    AddressRepository addressRepository;

    @Override
    public List<AddressDto> getAddresses(String userId) {
        List<AddressDto> returnValue = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();

        List<AddressEntity> addresses = addressRepository.findAllByUserDetailsUserId(userId);

        addresses.forEach(addressEntity -> returnValue.add(modelMapper.map(addressEntity, AddressDto.class)));

        return returnValue;
    }

    @Override
    public AddressDto getAddress(String addressId) {
        AddressDto returnValue = null;

        AddressEntity addressEntity = addressRepository.findByAddressId(addressId);
        if (addressEntity != null) {
            returnValue = new ModelMapper().map(addressEntity, AddressDto.class);
        }

        return returnValue;
    }
}
