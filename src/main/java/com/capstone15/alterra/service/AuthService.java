package com.capstone15.alterra.service;

import com.capstone15.alterra.config.security.JwtTokenProvider;
import com.capstone15.alterra.constant.AppConstant;
import com.capstone15.alterra.domain.dao.UserDao;
import com.capstone15.alterra.domain.dto.payload.TokenResponse;
import com.capstone15.alterra.domain.dto.payload.UsernamePassword;
import com.capstone15.alterra.domain.dto.payload.UsernamePasswordFGD;
import com.capstone15.alterra.repository.UserRepository;
import com.capstone15.alterra.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Optional;
import java.util.regex.Pattern;

@Log4j2
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper mapper;

    public ResponseEntity<Object> register(UsernamePasswordFGD req) {
        log.info("Executing register user with request: {}", req);
        try {
            final String PASSWORD_PATTERN =
                    "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{6,}$";  // (?=.*[!@#&()–[{}]:;',?/*~$^+=<>])

            final String USERNAME_PATTERN =
                    "^(?=.{6,20}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$";

            final String USERNAME_PATTERN2 =
                    "^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){4,18}[a-zA-Z0-9]$";

             final String EMAIL_ADDRESS_PATTERN =
                    "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";


            if(req.getUsername() == null) {
                return ResponseUtil.build("username cant null !", null, HttpStatus.BAD_REQUEST);
            }
            if(req.getPassword() == null) {
                return ResponseUtil.build("password cant null !", null, HttpStatus.BAD_REQUEST);
            }
            if(req.getEmail() == null) {
                return ResponseUtil.build("email cant null !", null, HttpStatus.BAD_REQUEST);
            }
            Optional<UserDao> userDao = userRepository.findByEmails(req.getEmail().toLowerCase(Locale.ROOT));
            if(userDao.isPresent()) {
                return ResponseUtil.build("email already registered !", null, HttpStatus.BAD_REQUEST);
            }
            Optional<UserDao> userDao1 = userRepository.findByUsername(req.getUsername().toLowerCase(Locale.ROOT));
            if(userDao1.isPresent()) {
                return ResponseUtil.build("user already taken !", null, HttpStatus.BAD_REQUEST);
            }
            if(req.getUsername().length() < 6) {
                return ResponseUtil.build("username must 6 character or more !", null, HttpStatus.BAD_REQUEST);
            }
            if(!req.getUsername().matches(USERNAME_PATTERN)) {
                return ResponseUtil.build("username not valid !", null, HttpStatus.BAD_REQUEST);
            }
            if(!req.getPassword().matches(PASSWORD_PATTERN)){
                return ResponseUtil.build("password must have at least 1 capital, 1 lower, 1 number, and 6 character or more !", null, HttpStatus.BAD_REQUEST);
            }
            if(!req.getEmail().matches(EMAIL_ADDRESS_PATTERN)){
                return ResponseUtil.build("email not valid !", null, HttpStatus.BAD_REQUEST);
            }


            UserDao user = new UserDao();
            user.setUsername(req.getUsername().toLowerCase(Locale.ROOT).replace(" ", ""));
            user.setPassword(passwordEncoder.encode(req.getPassword()));
            user.setEmail(req.getEmail().toLowerCase(Locale.ROOT));

            user = userRepository.save(user);
            return ResponseUtil.build(AppConstant.Message.SUCCESS, mapper.map(user, UsernamePasswordFGD.class), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Happened error when add user. Error: {}", e.getMessage());
            log.trace("Get error when add user. ", e);
            throw e;   }


    }

    public TokenResponse generateToken(UsernamePassword req) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            req.getUsername(),
                            req.getPassword()

                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtTokenProvider.generateToken(authentication);
            TokenResponse tokenResponse = new TokenResponse();
            tokenResponse.setToken(jwt);

            UserDao userDao = userRepository.getDistinctTopByUsername(req.getUsername());
            tokenResponse.setEmail(userDao.getEmail());
            tokenResponse.setUsername(userDao.getUsername());
            tokenResponse.setId(userDao.getId());

            return tokenResponse;

        } catch (BadCredentialsException e) {
            log.error("Bad credential", e);
            throw new RuntimeException(e.getMessage(), e);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}

