package com.capstone15.alterra.controller;

import com.capstone15.alterra.domain.dao.UserDao;
import com.capstone15.alterra.domain.dto.CommentLikeDto;
import com.capstone15.alterra.domain.dto.ThreadFollowerDto;
import com.capstone15.alterra.service.ThreadFollowerService;
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
@Api(tags = "Thread Follow")
public class ThreadFollowerController {

    @Autowired
    private ThreadFollowerService threadFollowerService;

    @ApiOperation(value = "Follow thread", response = ThreadFollowerDto.class )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success Follow thread"),
    })
    @PutMapping(value = "/thread/{id}")
    public ResponseEntity<Object> followThread( @PathVariable(value = "id") Long id) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return threadFollowerService.followThread(id, (UserDao) userDetails);
    }

    @ApiOperation(value = "Get follower by id thread", response = ThreadFollowerDto.class )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success Get follower by id thread"),
    })
    @GetMapping(value = "/thread/{id}")
    public ResponseEntity<Object> getByIdThread(@PathVariable(value = "id") Long id) {
        return threadFollowerService.getFollowerByIdThread(id);
    }

}
