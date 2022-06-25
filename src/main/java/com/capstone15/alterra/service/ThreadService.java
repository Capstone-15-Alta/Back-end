package com.capstone15.alterra.service;

import com.capstone15.alterra.constant.AppConstant;
import com.capstone15.alterra.domain.dao.*;
import com.capstone15.alterra.domain.dto.CommentDto;
import com.capstone15.alterra.domain.dto.ThreadDto;
import com.capstone15.alterra.domain.dto.ThreadDtoResponse;
import com.capstone15.alterra.domain.dto.UserDto;
import com.capstone15.alterra.repository.CategoryRepository;
import com.capstone15.alterra.repository.ThreadRepository;
import com.capstone15.alterra.repository.ThreadViewRepository;
import com.capstone15.alterra.repository.UserRepository;
import com.capstone15.alterra.util.FileUploadUtil;
import com.capstone15.alterra.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class ThreadService {

    @Autowired
    private ThreadRepository threadRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ThreadViewRepository threadViewRepository;

    @Value("${fgd-api.url}")
    private String apiUrl;

    @Autowired
    private ModelMapper mapper;

    public ResponseEntity<Object> addThread(ThreadDto request, MultipartFile multipartFile, UserDao user) throws IOException {
        log.info("Executing add thread with request: ");
        try{
            if(request.getTitle() == null) {
                return ResponseUtil.build("title cant null !", null, HttpStatus.BAD_REQUEST);
            }
            if(request.getDescription() == null) {
                return ResponseUtil.build("description cant null !", null, HttpStatus.BAD_REQUEST);
            }
            if(request.getCategoryId() == null) {
                return ResponseUtil.build("category cant null !", null, HttpStatus.BAD_REQUEST);
            }
            Optional<CategoryDao> categoryDao = categoryRepository.findById(request.getCategoryId());
            if(categoryDao.isEmpty()) {
                log.info("category id: {} not found", request.getCategoryId());
                return ResponseUtil.build("Category not found", null, HttpStatus.BAD_REQUEST);
            }

            if(multipartFile == null) {
                ThreadDao threadDao = ThreadDao.builder()
                        .title(request.getTitle())
                        .description(request.getDescription())
                        .category(CategoryDao.builder().id(request.getCategoryId()).build())
                        .createdBy(user.getUsername())
                        .user(UserDao.builder().id(user.getId()).build())
                        .thread_followers(0)
                        .thread_likes(0)
                        .view(ThreadViewDao.builder().views(0).build())
                        .build();
                threadDao = threadRepository.save(threadDao);

                Optional<UserDao> userDao = userRepository.findById(user.getId());
                Objects.requireNonNull(userDao.orElse(null)).setTotalThreads(threadRepository.countThreads(user.getId()));
                userRepository.save(userDao.get());

                log.info("Executing add thread success");
                return ResponseUtil.build(AppConstant.Message.SUCCESS, mapper.map(threadDao, ThreadDto.class), HttpStatus.OK);
            }
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            long size = multipartFile.getSize();
            String filecode = FileUploadUtil.saveFile(fileName, multipartFile);

            ThreadDao threadDao = ThreadDao.builder()
                    .title(request.getTitle())
                    .description(request.getDescription())
                    .category(CategoryDao.builder().id(request.getCategoryId()).build())
                    .fileName(fileName)
                    .size(size)
                    .image(apiUrl + "/images/" + filecode)
                    .createdBy(user.getUsername())
                    .user(UserDao.builder().id(user.getId()).build())
                    .thread_followers(0)
                    .thread_likes(0)
                    .view(ThreadViewDao.builder().views(0).build())
                    .build();
//            threadDao.setCreatedBy(user.getUsername());
//            threadDao.setUser(UserDao.builder().id(user.getId()).build());
            threadDao = threadRepository.save(threadDao);
            Optional<UserDao> userDao = userRepository.findById(user.getId());
            Objects.requireNonNull(userDao.orElse(null)).setTotalThreads(threadRepository.countThreads(user.getId()));
            userRepository.save(userDao.get());

            log.info("Executing add thread success");
            return ResponseUtil.build(AppConstant.Message.SUCCESS, mapper.map(threadDao, ThreadDto.class), HttpStatus.OK);

        } catch (Exception e) {
            log.error("Happened error when add thread. Error: {}", e.getMessage());
            log.trace("Get error when add thread. ", e);
            throw e;   }
    }

    public ResponseEntity<Object> getAllThread() {
        log.info("Executing get all thread.");
        try{
            List<ThreadDao> daoList = threadRepository.findAll();
            List<ThreadDtoResponse> list = new ArrayList<>();
            for(ThreadDao dao : daoList){
                list.add(mapper.map(dao, ThreadDtoResponse.class));
            }
            return ResponseUtil.build(AppConstant.Message.SUCCESS, list, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Happened error when get all thread. Error: {}", e.getMessage());
            log.trace("Get error when get all thread. ", e);
            throw e;
        }  }

    public ResponseEntity<Object> getThreadById(Long id) {
        log.info("Executing get thread by id: {} ", id);
        try {
            Optional<ThreadDao> threadDaoOptional = threadRepository.findById(id);
            if(threadDaoOptional.isEmpty()) {
                log.info("thread id: {} not found", id);
                return ResponseUtil.build(AppConstant.Message.NOT_FOUND, null, HttpStatus.BAD_REQUEST);
            }
            Optional<ThreadViewDao> threadViewDaoOptional = threadViewRepository.findById(threadDaoOptional.get().getView().getThreadId());
            threadViewDaoOptional.ifPresent(res -> {
                log.info("Executing view + 1");
                res.setViews(res.getViews()+1);
                threadViewRepository.save(res);
            });
            log.info("Executing get thread by id success");
            ThreadDtoResponse threadDtoResponse = mapper.map(threadDaoOptional, ThreadDtoResponse.class);

            return ResponseUtil.build(AppConstant.Message.SUCCESS, threadDtoResponse, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Happened error when get thread by id. Error: {}", e.getMessage());
            log.trace("Get error when get thread by id. ", e);
            throw e;
        }
    }

    public ResponseEntity<Object> getThreadByIdUser(Long id) {
        log.info("Executing get all thread.");
        try{
            Optional<UserDao> userDaoOptional = userRepository.findById(id);
            if(userDaoOptional.isEmpty()) {
                log.info("user id: {} not found", id);
                return ResponseUtil.build(AppConstant.Message.NOT_FOUND, null, HttpStatus.BAD_REQUEST);
            }
            List<ThreadDao> daoList = userDaoOptional.get().getThreads();
            List<ThreadDtoResponse> list = new ArrayList<>();
            for(ThreadDao dao : daoList){
                list.add(mapper.map(dao, ThreadDtoResponse.class));
            }
            return ResponseUtil.build(AppConstant.Message.SUCCESS, list, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Happened error when get all thread. Error: {}", e.getMessage());
            log.trace("Get error when get all thread. ", e);
            throw e;
        }  }

    public ResponseEntity<Object> searchThreadByTitle(String title) {
        try {
            log.info("Executing search thread by title: [{}]", title);

            List<ThreadDtoResponse> threadDtoList = new ArrayList<>();
            List<ThreadDao> threadDaos = threadRepository.findAllThreadByTitle(title);
            if(threadDaos.isEmpty()){
                return ResponseUtil.build(AppConstant.Message.NOT_FOUND, null, HttpStatus.NOT_FOUND);

            }
            for (ThreadDao threadDao : threadDaos) {
                threadDtoList.add(mapper.map(threadDao, ThreadDtoResponse.class));
            }
            return ResponseUtil.build(AppConstant.Message.SUCCESS, threadDtoList, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Happened error when search thread by title. Error: {}", e.getMessage());
            log.trace("Get error when search thread by title. ", e);
            throw e;
        }
    }

    public ResponseEntity<Object> searchThreadByCategoryName(String categoryName) {
        try {
            log.info("Executing search thread by category: [{}]", categoryName);
            List<ThreadDtoResponse> threadDtoList = new ArrayList<>();

            List<ThreadDao> threadDaos = threadRepository.findThreadDaoByCategoryCategoryName(categoryName);
            if(threadDaos.isEmpty()){
                List<ThreadDao> daoList = threadRepository.findAll();
                List<ThreadDtoResponse> list = new ArrayList<>();
                for(ThreadDao dao : daoList){
                    list.add(mapper.map(dao, ThreadDtoResponse.class));
                }
                return ResponseUtil.build(AppConstant.Message.SUCCESS, list, HttpStatus.OK);
            }
            for (ThreadDao threadDao : threadDaos) {
                threadDtoList.add(mapper.map(threadDao, ThreadDtoResponse.class));
            }
            return ResponseUtil.build(AppConstant.Message.SUCCESS, threadDtoList, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Happened error when search thread by title. Error: {}", e.getMessage());
            log.trace("Get error when search thread by title. ", e);
            throw e;
        }
    }

    public ResponseEntity<Object> searchTrendingThread() {
        try {
            log.info("Executing search trending thread");
            List<ThreadDtoResponse> threadDtoList = new ArrayList<>();

            List<ThreadDao> threadDaos = threadRepository.findAllPopularThread();
            if(threadDaos.isEmpty()){
                return ResponseUtil.build(AppConstant.Message.NOT_FOUND, null, HttpStatus.NOT_FOUND);
            }
            for (ThreadDao threadDao : threadDaos) {
                threadDtoList.add(mapper.map(threadDao, ThreadDtoResponse.class));
            }
            return ResponseUtil.build(AppConstant.Message.SUCCESS, threadDtoList, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Happened error when search trending thread. Error: {}", e.getMessage());
            log.trace("Get error when search trending thread. ", e);
            throw e;
        }
    }

    public ResponseEntity<Object> getAllThreadWithPaginate(Pageable pageable) {
        log.info("Executing get thread with paginate: {}", pageable);
        try{
            Page<ThreadDao> threadDaos = threadRepository.findAllBy(pageable);

            List<ThreadDtoResponse> list = new ArrayList<>();
            for(ThreadDao dao : threadDaos){
                list.add(mapper.map(dao, ThreadDtoResponse.class));
            }
            return ResponseUtil.build(AppConstant.Message.SUCCESS, list, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Happened error when get thread with paginate . Error: {}", e.getMessage());
            log.trace("Get error when get thread with paginate. ", e);
            throw e;
        }

    }

    public ResponseEntity<Object> deleteThread(Long id, UserDao user) {
        log.info("Executing delete thread id: {}", id);
        try{
            Optional<ThreadDao> threadDaoOptional = threadRepository.findById(id);
            if(threadDaoOptional.isEmpty()) {
                log.info("Thread {} not found", id);
                return ResponseUtil.build(AppConstant.Message.NOT_FOUND, null, HttpStatus.BAD_REQUEST);
            }
            if(!threadDaoOptional.get().getUser().getId().equals(user.getId()) && user.getRoles().equals("USER")) {
                log.info("Thread {} cant delete by user {}", id, user.getUsername());
                return ResponseUtil.build(AppConstant.Message.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            threadViewRepository.deleteById(threadDaoOptional.get().getView().getThreadId());
            threadRepository.deleteById(id);

            Optional<UserDao> userDao = userRepository.findById(user.getId());
            Objects.requireNonNull(userDao.orElse(null)).setTotalThreads(threadRepository.countThreads(user.getId()));
            userRepository.save(userDao.get());

            log.info("Executing delete thread success");
            return ResponseUtil.build(AppConstant.Message.SUCCESS, null, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Happened error when delete thread. Error: {}", e.getMessage());
            log.trace("Get error when delete thread. ", e);
            throw e;
        }
    }

    public ResponseEntity<Object> updateThread(Long id, ThreadDto request, MultipartFile multipartFile, UserDao user) throws IOException {
        log.info("Executing update thread with request: {}", request);
        try {
            Optional<ThreadDao> threadDaoOptional = threadRepository.findById(id);
            if(threadDaoOptional.isEmpty()) {
                log.info("thread {} not found", id);
                return ResponseUtil.build(AppConstant.Message.NOT_FOUND, null, HttpStatus.BAD_REQUEST);
            }
            if(!threadDaoOptional.get().getUser().getId().equals(user.getId()) && user.getRoles().equals("USER")) {
                log.info("Thread {} cant update by user {}", id, user.getUsername());
                return ResponseUtil.build(AppConstant.Message.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            if(multipartFile == null) {
                threadDaoOptional.ifPresent(res -> {
                    res.setTitle(request.getTitle());
                    res.setDescription(request.getDescription());
                    res.setCategory(CategoryDao.builder().id(request.getCategoryId()).build());

                    threadRepository.save(res);
                });
                log.info("Executing update thread success");
                return ResponseUtil.build(AppConstant.Message.SUCCESS, mapper.map(threadDaoOptional, ThreadDtoResponse.class), HttpStatus.OK);
            }
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            long size = multipartFile.getSize();
            String filecode = FileUploadUtil.saveFile(fileName, multipartFile);

            threadDaoOptional.ifPresent(res -> {
                res.setTitle(request.getTitle());
                res.setDescription(request.getDescription());
                res.setCategory(CategoryDao.builder().id(request.getCategoryId()).build());
                res.setFileName(fileName);
                res.setSize(size);
                res.setImage(apiUrl + "/images/" + filecode);
                threadRepository.save(res);
            });
            log.info("Executing update thread success");
            return ResponseUtil.build(AppConstant.Message.SUCCESS, mapper.map(threadDaoOptional, ThreadDtoResponse.class), HttpStatus.OK);

        } catch (Exception e) {
            log.error("Happened error when update thread. Error: {}", e.getMessage());
            log.trace("Get error when update thread. ", e);
            throw e;
        }
    }

}
