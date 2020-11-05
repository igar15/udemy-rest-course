package com.igar15.rest_course.controller;

import com.igar15.rest_course.exceptions.UserServiceException;
import com.igar15.rest_course.model.request.PasswordResetModel;
import com.igar15.rest_course.model.request.PasswordResetRequestModel;
import com.igar15.rest_course.model.request.UserDetailsRequestModel;
import com.igar15.rest_course.model.response.*;
import com.igar15.rest_course.service.AddressService;
import com.igar15.rest_course.service.UserService;
import com.igar15.rest_course.shared.dto.AddressDto;
import com.igar15.rest_course.shared.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AddressService addressService;

    @GetMapping("/{id}")
    public UserRest getUser(@PathVariable("id") String id) {
        UserDto userDto = userService.getUserByUserId(id);
//        BeanUtils.copyProperties(userDto, returnValue);
        UserRest returnValue = new ModelMapper().map(userDto, UserRest.class);
        return returnValue;
    }

    @PostMapping
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) {
//        UserRest returnValue = new UserRest();

        if (userDetails.getFirstName().isEmpty()) {
            throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
        }

//        UserDto userDto = new UserDto();
//        BeanUtils.copyProperties(userDetails, userDto);

        ModelMapper modelMapper = new ModelMapper();
        UserDto userDto = modelMapper.map(userDetails, UserDto.class);

        UserDto createdUser = userService.createUser(userDto);
//        BeanUtils.copyProperties(createdUser, returnValue);
        UserRest returnValue = modelMapper.map(createdUser, UserRest.class);
        return returnValue;
    }

    @PutMapping("/{id}")
    public UserRest updateUser(@PathVariable("id") String id, @RequestBody UserDetailsRequestModel userDetails) {
        UserRest returnValue = new UserRest();

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails, userDto);

        UserDto updatedUser = userService.updateUser(id, userDto);
        BeanUtils.copyProperties(updatedUser, returnValue);
        return returnValue;
    }

    @DeleteMapping("/{id}")
    public OperationStatusModel deleteUser(@PathVariable("id") String id) {
        OperationStatusModel operationStatusModel = new OperationStatusModel();

        userService.deleteUser(id);

        operationStatusModel.setOperationName(RequestOperationName.DELETE.name());
        operationStatusModel.setOperationResult(RequestOperationStatus.SUCCESS.name());
        return operationStatusModel;
    }

    @GetMapping
    public List<UserRest> getUsers(@SortDefault("lastName") Pageable pageable) {    // we can use @RequestParam(page & size) instead of Pageable, but what for?
        List<UserRest> returnValue = new ArrayList<>();
        List<UserDto> users = userService.getUsers(pageable);

        users.forEach(userDto -> {
            UserRest userRest = new UserRest();
            BeanUtils.copyProperties(userDto, userRest);
            returnValue.add(userRest);
        });

        return returnValue;
    }

    @GetMapping(value = "/{id}/addresses", produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    public CollectionModel<AddressRest> getUserAddresses(@PathVariable("id") String id) {
        List<AddressRest> returnValue = new ArrayList<>();

        List<AddressDto> addressesDto = addressService.getAddresses(id);

        if (addressesDto != null && !addressesDto.isEmpty()) {
            Type listType = new TypeToken<List<AddressRest>>() {}.getType();
            ModelMapper modelMapper = new ModelMapper();
            returnValue = modelMapper.map(addressesDto, listType);
        }

        returnValue.forEach(addressRest -> {
            Link addressLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserAddress(id, addressRest.getAddressId())).withSelfRel();
            Link userLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUser(id)).withRel("user");
            addressRest.add(addressLink, userLink);
        });

        return new CollectionModel<>(returnValue);
    }

    @GetMapping(value = "/{id}/addresses/{addressId}", produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    public EntityModel<AddressRest> getUserAddress(@PathVariable("id") String id, @PathVariable("addressId") String addressId) {
        AddressDto addressDto = addressService.getAddress(addressId);

        ModelMapper modelMapper = new ModelMapper();

        AddressRest returnValue = modelMapper.map(addressDto, AddressRest.class);

        //Link addressLink = WebMvcLinkBuilder.linkTo(UserController.class).slash(id).slash("addresses").slash(addressId).withSelfRel();
        Link addressLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserAddress(id, addressId)).withSelfRel();
//        Link userLink = WebMvcLinkBuilder.linkTo(UserController.class).slash(id).withRel("user");
        Link userLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUser(id)).withRel("user");
//        Link addressesLink = WebMvcLinkBuilder.linkTo(UserController.class).slash(id).slash("addresses").withRel("addresses");
        Link addressesLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserAddresses(id)).withRel("addresses");

        returnValue.add(addressLink);
        returnValue.add(userLink);
        returnValue.add(addressesLink);

        return new EntityModel<>(returnValue);
    }

    @GetMapping("/email-verification")
    public OperationStatusModel verifyEmailToken(@RequestParam(name = "token") String token) {
        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName(RequestOperationName.VERIFY_EMAIl.name());

        boolean isVerified = userService.verifyEmailToken(token);

        if (isVerified) {
            returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        } else {
            returnValue.setOperationResult(RequestOperationStatus.ERROR.name());
        }

        return returnValue;
    }

    @PostMapping("/password-reset-request")
    public OperationStatusModel requestReset(@RequestBody PasswordResetRequestModel passwordResetRequestModel) {
        OperationStatusModel returnValue = new OperationStatusModel();

        boolean operationResult = userService.requestPasswordReset(passwordResetRequestModel.getEmail());

        returnValue.setOperationName(RequestOperationName.REQUEST_PASSWORD_RESET.name());
        returnValue.setOperationResult(RequestOperationStatus.ERROR.name());

        if (operationResult) {
            returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        }

        return returnValue;
    }

    @PostMapping("/password-reset")
    public OperationStatusModel requestReset(@RequestBody PasswordResetModel passwordResetModel) {
        OperationStatusModel returnValue = new OperationStatusModel();

        boolean operationResult = userService.resetPassword(passwordResetModel.getToken(), passwordResetModel.getPassword());

        returnValue.setOperationName(RequestOperationName.PASSWORD_RESET.name());
        returnValue.setOperationResult(RequestOperationStatus.ERROR.name());

        if (operationResult) {
            returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        }

        return returnValue;
    }


}
