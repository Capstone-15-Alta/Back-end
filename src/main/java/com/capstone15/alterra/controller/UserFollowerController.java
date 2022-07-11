package com.capstone15.alterra.controller;

import com.capstone15.alterra.domain.dao.UserDao;
import com.capstone15.alterra.service.UserFollowerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/v1/follow", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserFollowerController {

    @Autowired
    private UserFollowerService userFollowerService;

    @PutMapping(value = "/user/{id}")
    public ResponseEntity<Object> followUser(@PathVariable(value = "id") Long id) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return userFollowerService.followUser(id, (UserDao) userDetails);
    }

    @GetMapping(value = "/user/{id}")
    public ResponseEntity<Object> getByIdUser(@PathVariable(value = "id") Long id) {
        return userFollowerService.getFollowerByIdUser(id);
    }

}
