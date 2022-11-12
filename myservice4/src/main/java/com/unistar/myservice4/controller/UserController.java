package com.unistar.myservice4.controller;

import java.util.ArrayList;
import java.util.List;

import com.unistar.myservice4.entity.UserEntity;
import com.unistar.myservice4.exceptions.UserServiceException;
import com.unistar.myservice4.model.*;
import com.unistar.myservice4.shared.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.unistar.myservice4.service.UserService;

@RestController
@RequestMapping("/v1/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<UserRest> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
                                   @RequestParam(value = "limit", defaultValue = "20") int limit) {
		
		logger.info("getUsers: page={}, limit={} ...", page, limit);
		
        // make page number from UI should be 1-based, while the DB is 0-based
        if(page > 0) {
            page--;
        }

        List<UserRest> returnValue = new ArrayList<>();
        List<UserDto> users = userService.getUsers(page, limit);

        ModelMapper modelMapper = new ModelMapper();
        for(UserDto userDto : users) {
            UserRest userModel = new UserRest();
            BeanUtils.copyProperties(userDto, userModel);
            //UserRest userModel = modelMapper.map(userDto, UserRest.class);

            returnValue.add(userModel);
        }

        return returnValue;
    }

    @GetMapping(path="/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public UserRest getUser(@PathVariable String id) {
		logger.info("getUser/{} ...", id);
		
        UserDto userDto = userService.getUserByUserId(id);
        //UserRest returnValue = new UserRest();
        //BeanUtils.copyProperties(userDto, returnValue);

        // also we can use org.modelmapper.ModelMapper to copy the properties instead of BeanUtils:
        ModelMapper modelMapper = new ModelMapper();
        UserRest returnValue = modelMapper.map(userDto, UserRest.class);

        return returnValue;
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {
		logger.info("createUser : {} ", userDetails.getEmail());
		
        if (userDetails.getFirstName().isEmpty())
            throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());

        if (userDetails.getLastName().isEmpty())
            throw new NullPointerException("The object is null");

        //UserDto userDto = new UserDto();
        //BeanUtils.copyProperties(userDetails, userDto);
        ModelMapper modelMapper = new ModelMapper();
        UserDto userDto = modelMapper.map(userDetails, UserDto.class);

        UserDto createdUser = userService.createUser(userDto);

        //UserRest returnValue = new UserRest();
        //BeanUtils.copyProperties(createdUser, returnValue);
        UserRest returnValue = modelMapper.map(createdUser, UserRest.class);

        return returnValue;
    }

    @PutMapping(path="/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public UserRest updateUser(@PathVariable String id, @RequestBody UserDetailsRequestModel userDetails) {
		logger.info("updateUser/{} ...", id);
		
        //UserDto userDto = new UserDto();
        //BeanUtils.copyProperties(userDetails, userDto);
        ModelMapper modelMapper = new ModelMapper();
        UserDto userDto = modelMapper.map(userDetails, UserDto.class);

        UserDto updatedUser = userService.updateUser(id, userDto);
        //UserRest returnValue = new UserRest();
        //BeanUtils.copyProperties(updatedUser, returnValue);
        UserRest returnValue = modelMapper.map(updatedUser, UserRest.class);

        return returnValue;
    }

    @DeleteMapping(path="/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public OperationStatusModel deleteUser(@PathVariable String id) {
		logger.info("deleteUser/{} ...", id);
		
        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName(RequestOperationName.DELETE.name());
        userService.deleteUser(id);
        returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        return returnValue;
    }
}
