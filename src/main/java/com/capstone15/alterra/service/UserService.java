package com.capstone15.alterra.service;

import com.capstone15.alterra.constant.AppConstant;
import com.capstone15.alterra.domain.dao.CategoryDao;
import com.capstone15.alterra.domain.dao.ThreadDao;
import com.capstone15.alterra.domain.dao.UserDao;
import com.capstone15.alterra.domain.dto.CategoryDto;
import com.capstone15.alterra.domain.dto.ThreadDtoResponse;
import com.capstone15.alterra.domain.dto.UserDto;
import com.capstone15.alterra.domain.dto.UserDtoResponse;
import com.capstone15.alterra.repository.UserRepository;
import com.capstone15.alterra.util.ResponseUtil;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.bytebuddy.utility.RandomString;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;

    @Value("${SENDGRID_API_KEY}")
    private String apiKey;

    @Value("${fgd-api.url}")
    private String apiUrl;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDao user = userRepository.getDistinctTopByUsername(username);
        if(user == null)
            throw new UsernameNotFoundException("Username not found");
        return user;
    }

    public ResponseEntity<Object> getAllUser() {
        log.info("Executing get all user.");
        try{
            List<UserDao> daoList = userRepository.findAll();
            List<UserDto> list = new ArrayList<>();
            for(UserDao dao : daoList){
                list.add(mapper.map(dao, UserDto.class));
            }
            return ResponseUtil.build(AppConstant.Message.SUCCESS, list, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Happened error when get all user. Error: {}", e.getMessage());
            log.trace("Get error when get all user. ", e);
            throw e;
        }
    }

    public ResponseEntity<Object> getUserById(Long id) {
        log.info("Executing get user by id: {} ", id);
        try {
            Optional<UserDao> userDaoOptional = userRepository.findById(id);
            if(userDaoOptional.isEmpty()) {
                log.info("user id: {} not found", id);
                return ResponseUtil.build(AppConstant.Message.NOT_FOUND, null, HttpStatus.BAD_REQUEST);
            }
            log.info("Executing get user by id success");
            UserDtoResponse userDto = mapper.map(userDaoOptional, UserDtoResponse.class);
            return ResponseUtil.build(AppConstant.Message.SUCCESS, userDto, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Happened error when get user by id. Error: {}", e.getMessage());
            log.trace("Get error when get user by id. ", e);
            throw e;
        }
    }

    public ResponseEntity<Object> changeRoleUser(Long id) {
        log.info("Executing change role user with id: {}", id);
        try {
            Optional<UserDao> userDao = userRepository.findById(id);
            if(userDao.isEmpty()) {
                log.info("user {} not found", id);
                return ResponseUtil.build(AppConstant.Message.NOT_FOUND, null, HttpStatus.BAD_REQUEST);
            }
            userDao.ifPresent(res -> {
                res.setRoles("USER");
                userRepository.save(res);
            });
            log.info("Executing change role user success");
            return ResponseUtil.build(AppConstant.Message.SUCCESS, mapper.map(userDao, UserDto.class), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Happened error when change role user. Error: {}", e.getMessage());
            log.trace("Get error when change role user. ", e);
            throw e;
        }
    }

    public ResponseEntity<Object> changeRoleModerator(Long id) {
        log.info("Executing change role moderator with id: {}", id);
        try {
            Optional<UserDao> userDao = userRepository.findById(id);
            if(userDao.isEmpty()) {
                log.info("user {} not found", id);
                return ResponseUtil.build(AppConstant.Message.NOT_FOUND, null, HttpStatus.BAD_REQUEST);
            }
            userDao.ifPresent(res -> {
                res.setRoles("MODERATOR");
                userRepository.save(res);
            });
            log.info("Executing change role moderator success");
            return ResponseUtil.build(AppConstant.Message.SUCCESS, mapper.map(userDao, UserDto.class), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Happened error when change role moderator. Error: {}", e.getMessage());
            log.trace("Get error when change role moderator. ", e);
            throw e;
        }
    }

    public void updateResetPasswordToken(String token, String email)  {
        try {
            UserDao customer = userRepository.findByEmail(email);
            if (customer != null) {
                customer.setResetPasswordToken(token);
                userRepository.save(customer);
            }
        } catch (Exception e) {
            log.error("Happened error when updateResetPasswordToken. Error: {}", e.getMessage());
            log.trace("Get error when updateResetPasswordToken. ", e);
            throw e;
        }
    }

    public UserDao getByResetPasswordToken(String token) {
        return userRepository.findByResetPasswordToken(token);
    }

    public void updatePassword(UserDao customer, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        customer.setPassword(encodedPassword);
        customer.setResetPasswordToken(null);
        userRepository.save(customer);
    }



    public ResponseEntity<Object> forgotPassword(HttpServletRequest req) throws IOException {
        // the sender email should be the same as we used to Create a Single Sender Verification
        String token = RandomString.make(30);
        updateResetPasswordToken(token, req.getParameter("email"));
        Email from = new Email("fgdcapstone@gmail.com");
        String subject = "Reset Password FGD";
        Email to = new Email(req.getParameter("email"));

        String resetPasswordLink = apiUrl + "/v1/user/reset_password?token=" + token;

        Content content = new Content("text/plain", "This is a reset password link : " + "\n" + resetPasswordLink);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(apiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);

            return ResponseUtil.build(AppConstant.Message.SUCCESS, response.getBody(), HttpStatus.OK);
        } catch (IOException ex) {
            throw ex;
        }
    }
}