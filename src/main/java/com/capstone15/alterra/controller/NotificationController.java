package com.capstone15.alterra.controller;

import com.capstone15.alterra.domain.dao.UserDao;
import com.capstone15.alterra.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/v1/notification", produces = MediaType.APPLICATION_JSON_VALUE)
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping(value = "")
    public ResponseEntity<Object> getAll(@PageableDefault( page = 0, size = 10) Pageable pageable) {

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return notificationService.getAllNotification((UserDao) userDetails, pageable);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> readById(@PathVariable(value = "id") Long id){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return notificationService.readNotificationById(id, (UserDao) userDetails);
    }

    @GetMapping(value = "/readall")
    public ResponseEntity<Object> readAll(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return notificationService.readAllNotification((UserDao) userDetails);
    }

}
