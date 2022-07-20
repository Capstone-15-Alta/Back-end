package com.capstone15.alterra.controller;

import com.capstone15.alterra.domain.dao.UserDao;
import com.capstone15.alterra.domain.dto.CommentDto;
import com.capstone15.alterra.domain.dto.CommentLikeDto;
import com.capstone15.alterra.service.CommentLikeService;
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
@Api(tags = "Comment Like")
public class CommentLikeController {

    @Autowired
    private CommentLikeService commentLikeService;

    @ApiOperation(value = "Like comment", response = CommentLikeDto.class )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success Like comment"),
    })
    @PutMapping(value = "/comment/{id}")
    public ResponseEntity<Object> likeComment(@PathVariable(value = "id") Long id) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return commentLikeService.likeComment(id, (UserDao) userDetails);
    }

    @ApiOperation(value = "Get like by id comment", response = CommentLikeDto.class )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success Get like by id comment"),
    })
    @GetMapping(value = "/comment/{id}")
    public ResponseEntity<Object> getByIdThread(@PathVariable(value = "id") Long id) {
        return commentLikeService.getLikeByIdComment(id);
    }
}
