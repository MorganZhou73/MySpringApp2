package com.unistar.myservice4.service.impl;

import com.unistar.myservice4.exceptions.ResourceNotFoundException;
import com.unistar.myservice4.exceptions.UserServiceException;
import com.unistar.myservice4.model.ErrorMessages;
import com.unistar.myservice4.shared.Utils;
import com.unistar.myservice4.shared.dto.UserDto;
import com.unistar.myservice4.entity.UserEntity;
import com.unistar.myservice4.repository.UserRepository;
import com.unistar.myservice4.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    Utils utils;

//    @Autowired
//    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDto createUser(UserDto user) {
        if(userRepository.findByEmail(user.getEmail()) != null)
            throw new UserServiceException("Record already exists");

        ModelMapper modelMapper = new ModelMapper();
        UserEntity userEntity = modelMapper.map(user, UserEntity.class);

        // set backend processed properties
        String publicUserId = utils.generateUserId(20);
        userEntity.setUserId(publicUserId);

        // to simplify, not encrypt
        //userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userEntity.setEncryptedPassword(user.getPassword());

        UserEntity storedUserDetails = userRepository.save(userEntity);

        UserDto returnValue = modelMapper.map(storedUserDetails, UserDto.class);

        return returnValue;
    }

    @Override
    public UserDto getUser(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if(userEntity == null)
            throw new ResourceNotFoundException("email : " + email);

        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(userEntity, returnValue);
        //ModelMapper modelMapper = new ModelMapper();
        //UserDto returnValue = modelMapper.map(userEntity, UserDto.class);
        return returnValue;
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);

        // just show different ways for exceptions
        if(userEntity == null)
            throw new UserServiceException("User with ID: " + userId + " not found");

        //UserDto returnValue = new UserDto();
        //BeanUtils.copyProperties(userEntity, returnValue);
        ModelMapper modelMapper = new ModelMapper();
        UserDto returnValue = modelMapper.map(userEntity, UserDto.class);

        return returnValue;
    }

    @Override
    public UserDto updateUser(String userId, UserDto user) {
        UserEntity userEntity = userRepository.findByUserId(userId);
        if(userEntity == null)
            throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage() + userId);

        // assume backend business not permit change email
        if(user.getFirstName() != null)
            userEntity.setFirstName(user.getFirstName());

        if(user.getLastName() != null)
            userEntity.setLastName(user.getLastName());

        if(user.getPassword() != null) {
            // not encrypt for the time being
            //userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            userEntity.setEncryptedPassword(user.getPassword());
        }

        UserEntity updatedUserDetails = userRepository.save(userEntity);
        //UserDto returnValue = new UserDto();
        //BeanUtils.copyProperties(updatedUserDetails, returnValue);
        ModelMapper modelMapper = new ModelMapper();
        UserDto returnValue = modelMapper.map(updatedUserDetails, UserDto.class);

        return returnValue;
    }

    @Override
    public void deleteUser(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);
        if(userEntity == null)
            throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

        userRepository.delete(userEntity);
    }

    @Override
    public List<UserDto> getUsers(int page, int limit) {
        List<UserDto> returnValue = new ArrayList<>() ;

        Pageable pageableRequest = PageRequest.of(page, limit);
        Page<UserEntity> userPage = userRepository.findAll(pageableRequest);
        List<UserEntity> users = userPage.getContent();

        //ModelMapper modelMapper = new ModelMapper();
        for(UserEntity userEntity : users) {
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(userEntity, userDto);
            //UserDto userDto = modelMapper.map(userEntity, UserDto.class);
            returnValue.add(userDto);
        }

        return returnValue;
    }
}
