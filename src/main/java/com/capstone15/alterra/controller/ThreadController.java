package com.capstone15.alterra.controller;

import com.capstone15.alterra.domain.dao.UserDao;
import com.capstone15.alterra.domain.dto.ThreadDto;
import com.capstone15.alterra.domain.dto.UserDto;
import com.capstone15.alterra.service.ThreadService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(value = "/v1/thread", produces = MediaType.APPLICATION_JSON_VALUE)
public class ThreadController {

    @Autowired
    private ThreadService threadService;

//    @PostMapping(value = "")
//    public ResponseEntity<Object> addThread(@RequestParam("title") String title,
//                                            @RequestParam("description") String description,
//                                            @RequestParam(value = "category_id") Long category_id,
//                                            @RequestParam(value = "file", required = false) MultipartFile multipartFile ) throws IOException {
//        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
//                .getPrincipal();
//        return threadService.addThread(title, description, category_id, multipartFile, (UserDao) userDetails);
//    }

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> addThread(@RequestParam("json") String request,
                                            @RequestParam(value = "file", required = false) MultipartFile multipartFile ) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        ThreadDto threadDto = mapper.readValue(request, ThreadDto.class);

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return threadService.addThread(threadDto, multipartFile, (UserDao) userDetails);
    }

    @GetMapping(value = "")
    public ResponseEntity<Object> getAll() {
        return threadService.getAllThread();
    }

    @GetMapping(value = "/pages")
    public ResponseEntity<Object> getAllWithPaginate(@PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC, page = 0, size = 5)  Pageable pageable) {
        return threadService.getAllThreadWithPaginate(pageable);
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getById(@PathVariable(value = "id") Long id){
        return threadService.getThreadById(id);
    }

    @GetMapping(value = "/user/{id}")
    public ResponseEntity<Object> getByIdUser(@PathVariable(value = "id") Long id){
        return threadService.getThreadByIdUser(id);
    }

    @GetMapping(value = "/search")
    public ResponseEntity<Object> searchThreadByTitle(@RequestParam(value = "title", required = false) String title){
        return threadService.searchThreadByTitle(title);
    }

    @GetMapping(value = "/category/{categoryName}")
    public ResponseEntity<Object> searchThreadByCategoryName(@PathVariable(value = "categoryName") String categoryName){
        return threadService.searchThreadByCategoryName(categoryName);
    }

    @GetMapping(value = "/trending")
    public ResponseEntity<Object> searchTrendingThread(){
        return threadService.searchTrendingThread();
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
