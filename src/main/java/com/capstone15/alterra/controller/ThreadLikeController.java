package com.capstone15.alterra.controller;

import com.capstone15.alterra.domain.dao.UserDao;
import com.capstone15.alterra.service.ThreadLikeService;
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
public class ThreadLikeController {

    @Autowired
    private ThreadLikeService threadLikeService;

    @PatchMapping(value = "/thread/{id}")
    public ResponseEntity<Object> followThread(@PathVariable(value = "id") Long id) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return threadLikeService.likeThread(id, (UserDao) userDetails);
    }

    @GetMapping(value = "/thread/{id}")
    public ResponseEntity<Object> getByIdThread(@PathVariable(value = "id") Long id) {
        return threadLikeService.getLikeByIdThread(id);
    }
}
