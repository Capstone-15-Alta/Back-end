package com.capstone15.alterra.service;

import com.capstone15.alterra.constant.AppConstant;
import com.capstone15.alterra.domain.dao.ThreadDao;
import com.capstone15.alterra.domain.dao.UserDao;
import com.capstone15.alterra.domain.dto.ThreadDtoResponse;
import com.capstone15.alterra.domain.dto.UserDto;
import com.capstone15.alterra.repository.UserRepository;
import com.capstone15.alterra.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDao user = userRepository.getDistinctTopByUsername(username);
        if(user == null)
            throw new UsernameNotFoundException("Username not found");
        return user;
    }

    public ResponseEntity<Object> getAllUser() {
        log.info("Executing get all user.");
        try{
            List<UserDao> daoList = userRepository.findAll();
            List<UserDto> list = new ArrayList<>();
            for(UserDao dao : daoList){
                list.add(mapper.map(dao, UserDto.class));
            }
            return ResponseUtil.build(AppConstant.Message.SUCCESS, list, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Happened error when get all user. Error: {}", e.getMessage());
            log.trace("Get error when get all user. ", e);
            throw e;
        }
    }

    public ResponseEntity<Object> getUserById(Long id) {
        log.info("Executing get user by id: {} ", id);
        try {
            Optional<UserDao> userDaoOptional = userRepository.findById(id);
            if(userDaoOptional.isEmpty()) {
                log.info("user id: {} not found", id);
                return ResponseUtil.build(AppConstant.Message.NOT_FOUND, null, HttpStatus.BAD_REQUEST);
            }
            log.info("Executing get user by id success");
            UserDto userDto = mapper.map(userDaoOptional, UserDto.class);
            return ResponseUtil.build(AppConstant.Message.SUCCESS, userDto, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Happened error when get user by id. Error: {}", e.getMessage());
            log.trace("Get error when get user by id. ", e);
            throw e;
        }
    }
}