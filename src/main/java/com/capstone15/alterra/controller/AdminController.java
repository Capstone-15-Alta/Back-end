package com.capstone15.alterra.controller;

import com.capstone15.alterra.domain.dao.UserDao;
import com.capstone15.alterra.domain.dto.ThreadDto;
import com.capstone15.alterra.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/v1/admin", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminController {

    @Autowired
    private UserService userService;

    @PutMapping(value = "/role/user/{id}")
    public ResponseEntity<Object> changeRoleUser(@PathVariable(value = "id") Long id) {
        return userService.changeRoleUser(id);
    }

    @PutMapping(value = "/role/moderator/{id}")
    public ResponseEntity<Object> changeRoleModerator(@PathVariable(value = "id") Long id) {
        return userService.changeRoleModerator(id);
    }

    @DeleteMapping(value = "/user/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable(value = "id") Long id) {
        return userService.deleteUser(id);
    }

}
