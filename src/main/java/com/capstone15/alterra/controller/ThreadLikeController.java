package com.capstone15.alterra.controller;

import com.capstone15.alterra.domain.dao.UserDao;
import com.capstone15.alterra.domain.dto.ThreadFollowerDto;
import com.capstone15.alterra.domain.dto.ThreadLikeDto;
import com.capstone15.alterra.service.ThreadLikeService;
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
@RequestMapping(value = "/v1/like", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Thread Like")
public class ThreadLikeController {

    @Autowired
    private ThreadLikeService threadLikeService;

    @ApiOperation(value = "Like thread", response = ThreadLikeDto.class )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success Like thread"),
    })
    @PutMapping(value = "/thread/{id}")
    public ResponseEntity<Object> likeThread(@PathVariable(value = "id") Long id) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return threadLikeService.likeThread(id, (UserDao) userDetails);
    }

    @ApiOperation(value = "Get like by id thread", response = ThreadLikeDto.class )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success Get like by id thread"),
    })
    @GetMapping(value = "/thread/{id}")
    public ResponseEntity<Object> getByIdThread(@PathVariable(value = "id") Long id) {
        return threadLikeService.getLikeByIdThread(id);
    }
}
