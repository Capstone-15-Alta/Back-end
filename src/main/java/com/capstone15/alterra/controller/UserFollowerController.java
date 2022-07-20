package com.capstone15.alterra.controller;

import com.capstone15.alterra.domain.dao.UserDao;
import com.capstone15.alterra.domain.dto.ThreadFollowerDto;
import com.capstone15.alterra.domain.dto.UserFollowerDto;
import com.capstone15.alterra.service.UserFollowerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
@Api(tags = "User Follow")
public class UserFollowerController {

    @Autowired
    private UserFollowerService userFollowerService;

    @ApiOperation(value = "Follow user", response = UserFollowerDto.class )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success Follow user"),
    })
    @PutMapping(value = "/user/{id}")
    public ResponseEntity<Object> followUser(@PathVariable(value = "id") Long id) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return userFollowerService.followUser(id, (UserDao) userDetails);
    }

    @ApiOperation(value = "Get follower by id user", response = UserFollowerDto.class )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success Get follower by id user"),
    })
    @GetMapping(value = "/user/{id}")
    public ResponseEntity<Object> getByIdUser(@PathVariable(value = "id") Long id) {
        return userFollowerService.getFollowerByIdUser(id);
    }

    @ApiOperation(value = "Get following by id user", response = UserFollowerDto.class )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success Get following by id user"),
    })
    @GetMapping(value = "/userFollowing/{id}")
    public ResponseEntity<Object> getByIdUserFollowing(@PathVariable(value = "id") Long id) {
        return userFollowerService.getFollowingByIdUser(id);
    }

}
