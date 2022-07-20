package com.capstone15.alterra.service;

import com.capstone15.alterra.constant.AppConstant;
import com.capstone15.alterra.domain.dao.UserDao;
import com.capstone15.alterra.domain.dto.UserDto;
import com.capstone15.alterra.domain.dto.UserDtoResponse;
import com.capstone15.alterra.repository.NotificationRepository;
import com.capstone15.alterra.repository.ThreadRepository;
import com.capstone15.alterra.repository.UserFollowerRepository;
import com.capstone15.alterra.repository.UserRepository;
import com.capstone15.alterra.util.FileUploadUtil;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    private ThreadRepository threadRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserFollowerRepository userFollowerRepository;

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

    public ResponseEntity<Object> getAllUser(Pageable pageable) {
        log.info("Executing get all user.");
        try{
            Page<UserDao> daoList = userRepository.findAll(pageable);
            if(daoList.isEmpty()) {
                log.info("user not found");
                return ResponseUtil.build(AppConstant.Message.NOT_FOUND, null, HttpStatus.BAD_REQUEST);
            }
            Page<UserDtoResponse> userDtoResponses = daoList.map(userDao -> mapper.map(userDao, UserDtoResponse.class));

            return ResponseUtil.build(AppConstant.Message.SUCCESS, userDtoResponses, HttpStatus.OK);
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

            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);

            return ResponseUtil.build(AppConstant.Message.SUCCESS, response.getBody(), HttpStatus.OK);

    }

    public ResponseEntity<Object> deleteUser(Long id) {
        log.info("Executing delete user id: {}", id);
        try{
            Optional<UserDao> userDao = userRepository.findById(id);
            if(userDao.isEmpty()) {
                log.info("user {} not found", id);
                return ResponseUtil.build(AppConstant.Message.NOT_FOUND, null, HttpStatus.BAD_REQUEST);
            }

            threadRepository.deleteCustom(id);
            notificationRepository.deleteAllNotification(id);
            userFollowerRepository.deleteAll(id);
            userFollowerRepository.deleteAll2(id);
            userRepository.deleteById(id);
            log.info("Executing delete user success");
            return ResponseUtil.build(AppConstant.Message.SUCCESS, null, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Happened error when delete user. Error: {}", e.getMessage());
            log.trace("Get error when delete user. ", e);
            throw e;
        }

    }
    

    public ResponseEntity<Object> updateUserInfo(UserDtoResponse request, MultipartFile photo, MultipartFile cover, UserDao user) throws IOException {
        log.info("Executing update user with request: {}", request);
        try {
            Optional<UserDao> userDao = userRepository.findById(user.getId());
            if(userDao.isEmpty()) {
                log.info("user {} not found", user.getId());
                return ResponseUtil.build(AppConstant.Message.NOT_FOUND, null, HttpStatus.BAD_REQUEST);
            }
            if(!userDao.get().getId().equals(user.getId()) && user.getRoles().equals("USER")) {
                log.info("User {} cant update by user {}", user.getId(), user.getUsername());
                return ResponseUtil.build(AppConstant.Message.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            if(photo != null && cover != null) {
                String fileName = StringUtils.cleanPath(Objects.requireNonNull(photo.getOriginalFilename()));
                long size = photo.getSize();
                String filecode = FileUploadUtil.saveFile(fileName, photo);

                String fileNameCover = StringUtils.cleanPath(Objects.requireNonNull(cover.getOriginalFilename()));
                long sizeCover = cover.getSize();
                String filecodeCover = FileUploadUtil.saveFile(fileNameCover, cover);

                userDao.get().setFileNameCover(fileNameCover);
                userDao.get().setSizeCover(sizeCover);
                userDao.get().setImageCover(apiUrl + "/images/" + filecodeCover);
                userDao.get().setFileName(fileName);
                userDao.get().setSize(size);
                userDao.get().setImage(apiUrl + "/images/" + filecode);
            }

            if(photo != null) {
                String fileName = StringUtils.cleanPath(Objects.requireNonNull(photo.getOriginalFilename()));
                long size = photo.getSize();
                String filecode = FileUploadUtil.saveFile(fileName, photo);

                userDao.get().setFileName(fileName);
                userDao.get().setSize(size);
                userDao.get().setImage(apiUrl + "/images/" + filecode);

            }

            if(cover != null) {
                String fileNameCover = StringUtils.cleanPath(Objects.requireNonNull(cover.getOriginalFilename()));
                long sizeCover = cover.getSize();
                String filecodeCover = FileUploadUtil.saveFile(fileNameCover, cover);

                userDao.get().setFileNameCover(fileNameCover);
                userDao.get().setSizeCover(sizeCover);
                userDao.get().setImageCover(apiUrl + "/images/" + filecodeCover);
            }

            userDao.get().setPhone(request.getPhone());
            userDao.get().setEmail(request.getEmail());
            userDao.get().setFirstName(request.getFirstName());
            userDao.get().setLastName(request.getLastName());
            userDao.get().setBirthDate(request.getBirthDate());
            userDao.get().setEducation(request.getEducation());
            userDao.get().setGender(request.getGender());
            userDao.get().setCountry(request.getCountry());
            userDao.get().setCity(request.getCity());
            userDao.get().setZipCode(request.getZipCode());
            userRepository.save(userDao.get());

            log.info("Executing update user success");
            return ResponseUtil.build(AppConstant.Message.SUCCESS, mapper.map(userDao, UserDtoResponse.class), HttpStatus.OK);

        } catch (Exception e) {
            log.error("Happened error when update user. Error: {}", e.getMessage());
            log.trace("Get error when update user. ", e);
            throw e;
        }
    }

    public ResponseEntity<Object> updateUserPhoto( MultipartFile multipartFile, UserDao user) throws IOException {
        log.info("Executing update user photo");
        try {
            Optional<UserDao> userDao = userRepository.findById(user.getId());
            if (userDao.isEmpty()) {
                log.info("user {} not found", user.getId());
                return ResponseUtil.build(AppConstant.Message.NOT_FOUND, null, HttpStatus.BAD_REQUEST);
            }
            if (!userDao.get().getId().equals(user.getId()) && user.getRoles().equals("USER")) {
                log.info("User {} cant update by user {}", user.getId(), user.getUsername());
                return ResponseUtil.build(AppConstant.Message.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            long size = multipartFile.getSize();
            String filecode = FileUploadUtil.saveFile(fileName, multipartFile);

            Objects.requireNonNull(userDao.orElse(null)).setFileName(fileName);
            Objects.requireNonNull(userDao.orElse(null)).setSize(size);
            Objects.requireNonNull(userDao.orElse(null)).setImage(apiUrl + "/images/" + filecode);
            userRepository.save(userDao.get());

            log.info("Executing update user photo success");
            return ResponseUtil.build(AppConstant.Message.SUCCESS,  mapper.map(userDao, UserDtoResponse.class), HttpStatus.OK);

        } catch (Exception e) {
            log.error("Happened error when update user photo. Error: {}", e.getMessage());
            log.trace("Get error when update user photo. ", e);
            throw e;
        }
    }

    public ResponseEntity<Object> updateUserCover( MultipartFile multipartFile, UserDao user) throws IOException {
        log.info("Executing update user cover");
        try {
            Optional<UserDao> userDao = userRepository.findById(user.getId());
            if (userDao.isEmpty()) {
                log.info("user {} not found", user.getId());
                return ResponseUtil.build(AppConstant.Message.NOT_FOUND, null, HttpStatus.BAD_REQUEST);
            }
            if (!userDao.get().getId().equals(user.getId()) && user.getRoles().equals("USER")) {
                log.info("User {} cant update by user {}", user.getId(), user.getUsername());
                return ResponseUtil.build(AppConstant.Message.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            long size = multipartFile.getSize();
            String filecode = FileUploadUtil.saveFile(fileName, multipartFile);

            Objects.requireNonNull(userDao.orElse(null)).setFileNameCover(fileName);
            Objects.requireNonNull(userDao.orElse(null)).setSizeCover(size);
            Objects.requireNonNull(userDao.orElse(null)).setImageCover(apiUrl + "/images/" + filecode);
            userRepository.save(userDao.get());

            log.info("Executing update user cover success");
            return ResponseUtil.build(AppConstant.Message.SUCCESS,  mapper.map(userDao, UserDtoResponse.class), HttpStatus.OK);

        } catch (Exception e) {
            log.error("Happened error when update user cover. Error: {}", e.getMessage());
            log.trace("Get error when update user cover. ", e);
            throw e;
        }
    }

    public ResponseEntity<Object> getUserRankingByFollowers(Pageable pageable) {
        try {
            log.info("Executing search user by ranking");
            Page<UserDao> userDaos = userRepository.findAllUserByRanking(pageable);
            if(userDaos.isEmpty()){
                return ResponseUtil.build(AppConstant.Message.NOT_FOUND, null, HttpStatus.NOT_FOUND);
            }
            Page<UserDto> userDtos = userDaos.map(userDao -> mapper.map(userDao, UserDto.class));

            return ResponseUtil.build(AppConstant.Message.SUCCESS, userDtos, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Happened error when search user by ranking. Error: {}", e.getMessage());
            log.trace("Get error when search user by ranking. ", e);
            throw e;
        }
    }
    public ResponseEntity<Object> getUserRankingByTotalThreadAndLike(Pageable pageable) {
        try {
            log.info("Executing search user by ranking");
            Page<UserDao> userDaos = userRepository.findUserRanking(pageable);

            if(userDaos.isEmpty()){
                return ResponseUtil.build(AppConstant.Message.NOT_FOUND, null, HttpStatus.NOT_FOUND);
            }
            Page<UserDto> userDtos = userDaos.map(userDao -> mapper.map(userDao, UserDto.class));

            return ResponseUtil.build(AppConstant.Message.SUCCESS, userDtos, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Happened error when search user by ranking. Error: {}", e.getMessage());
            log.trace("Get error when search user by ranking. ", e);
            throw e;
        }
    }

}