package com.capstone15.alterra.controller;

import com.capstone15.alterra.domain.dao.UserDao;
import com.capstone15.alterra.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public String send(HttpServletRequest req) throws IOException {
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
    public String processResetPassword(HttpServletRequest request) {
        String token = request.getParameter("token");
        String password = request.getParameter("password");

        UserDao customer = userService.getByResetPasswordToken(token);
        if (customer == null) {
            return "Invalid Token";
        } else {
            userService.updatePassword(customer, password);
        }
        return "You have successfully changed your password.";
    }

}
