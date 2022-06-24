package com.capstone15.alterra.controller;

import com.capstone15.alterra.domain.dao.UserDao;
import com.capstone15.alterra.domain.dto.ThreadDto;
import com.capstone15.alterra.domain.dto.UserDtoResponse;
import com.capstone15.alterra.service.UserService;
import com.capstone15.alterra.util.ResponseUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping(value = "/v1/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "")
    public ResponseEntity<Object> getAll() {
        return userService.getAllUser();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getById(@PathVariable(value = "id") Long id){
        return userService.getUserById(id);}

    @PostMapping("/forgot_password")
    public ResponseEntity<Object> send(HttpServletRequest req) throws IOException {
        return userService.forgotPassword(req);
    }

    @GetMapping("/reset_password")
    public String showResetPasswordForm(@Param(value = "token") String token) {
        UserDao customer = userService.getByResetPasswordToken(token);

        if (customer == null) {
            return "Invalid Token";
        }
        return "reset_password_form";
    }

    @PostMapping("/reset_password")
    public ResponseEntity<Object> processResetPassword(HttpServletRequest request) {
        String token = request.getParameter("token");
        String password = request.getParameter("password");

        UserDao customer = userService.getByResetPasswordToken(token);
        if (customer == null) {
            return ResponseUtil.build("Invalid Token", null, HttpStatus.BAD_REQUEST);
        } else {
            userService.updatePassword(customer, password);
        }
        return ResponseUtil.build("You have successfully changed your password.", null, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> updateUser(@PathVariable(value = "id") Long id, @RequestParam("json") String request,
                                             @RequestParam(value = "file", required = false) MultipartFile multipartFile) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        UserDtoResponse userDtoResponse = mapper.readValue(request, UserDtoResponse.class);

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return userService.updateUser(id, userDtoResponse, multipartFile, (UserDao) userDetails);
    }

    @GetMapping(value = "/rank")
    public ResponseEntity<Object> getUserByRankingFollower(){
        return userService.getUserByRanking();
    }


}
