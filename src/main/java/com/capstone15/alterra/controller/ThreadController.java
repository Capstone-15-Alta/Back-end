package com.capstone15.alterra.controller;

import com.capstone15.alterra.domain.dao.UserDao;
import com.capstone15.alterra.domain.dto.ThreadDto;
import com.capstone15.alterra.domain.dto.UserDto;
import com.capstone15.alterra.service.ThreadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/v1/thread", produces = MediaType.APPLICATION_JSON_VALUE)
public class ThreadController {

    @Autowired
    private ThreadService threadService;

    @PostMapping(value = "")
    public ResponseEntity<Object> addThread(@RequestBody ThreadDto request) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return threadService.addThread(request, (UserDao) userDetails);
    }

    @GetMapping(value = "")
    public ResponseEntity<Object> getAll() {
        return threadService.getAllThread();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getById(@PathVariable(value = "id") Long id){
        return threadService.getThreadById(id);
    }

    @GetMapping(value = "/user/{id}")
    public ResponseEntity<Object> getByIdUser(@PathVariable(value = "id") Long id){
        return threadService.getThreadByIdUser(id);
    }


    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deleteThread(@PathVariable(value = "id") Long id) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return threadService.deleteThread(id, (UserDao) userDetails);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Object> updateThread(@PathVariable(value = "id") Long id, @RequestBody ThreadDto request) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return threadService.updateThread(id, request, (UserDao) userDetails);
    }
}
