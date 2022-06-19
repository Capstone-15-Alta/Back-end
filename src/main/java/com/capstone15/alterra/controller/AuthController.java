package com.capstone15.alterra.controller;

import com.capstone15.alterra.constant.AppConstant;
import com.capstone15.alterra.domain.dao.UserDao;
import com.capstone15.alterra.domain.dto.UserDto;
import com.capstone15.alterra.domain.dto.payload.TokenResponse;
import com.capstone15.alterra.domain.dto.payload.UsernamePassword;
import com.capstone15.alterra.domain.dto.payload.UsernamePasswordFGD;
import com.capstone15.alterra.service.AuthService;
import com.capstone15.alterra.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(value="/register")
    public ResponseEntity<?> register(@RequestBody UsernamePasswordFGD req) {
        authService.register(req);
        return ResponseUtil.build(AppConstant.Message.SUCCESS, req, HttpStatus.OK);
    }

    @PostMapping(value="/login" )
    public ResponseEntity<?> login(@RequestBody UsernamePassword req) {
        TokenResponse token = authService.generateToken(req);
        return ResponseUtil.build(AppConstant.Message.SUCCESS, token, HttpStatus.OK);
    }
}
