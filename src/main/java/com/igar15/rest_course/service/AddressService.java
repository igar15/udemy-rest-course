package com.igar15.rest_course.service;

import com.igar15.rest_course.shared.dto.AddressDto;

import java.util.List;

public interface AddressService {

    List<AddressDto> getAddresses(String userId);

    AddressDto getAddress(String addressId);
}
