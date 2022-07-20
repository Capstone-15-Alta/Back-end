package com.capstone15.alterra.controller;

import com.capstone15.alterra.domain.dao.UserDao;
import com.capstone15.alterra.domain.dto.SaveThreadDto;
import com.capstone15.alterra.domain.dto.ThreadDto;
import com.capstone15.alterra.domain.dto.ThreadDtoResponse;
import com.capstone15.alterra.service.ThreadService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

@Slf4j
@RestController
@RequestMapping(value = "/v1/thread", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Thread")
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

    @ApiOperation(value = "Add new thread", response = ThreadDto.class )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success add new thread"),
    })
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> addThread(@RequestParam("json") String request,
                                            @RequestParam(value = "file", required = false) MultipartFile multipartFile ) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        ThreadDto threadDto = mapper.readValue(request, ThreadDto.class);

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return threadService.addThread(threadDto, multipartFile, (UserDao) userDetails);
    }

//    @GetMapping(value = "")
//    public ResponseEntity<Object> getAll(@PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC, page = 0, size = 5)  Pageable pageable) {
//        return threadService.getAllThread(pageable);
//    }


    @ApiOperation(value = "Get thread by id", response = ThreadDtoResponse.class )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success Get thread by id"),
    })
    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getById(@PathVariable(value = "id") Long id){
        return threadService.getThreadById(id);
    }

    @ApiOperation(value = "Get thread by id user", response = ThreadDtoResponse.class )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success Get thread by id user"),
    })
    @GetMapping(value = "/user/{id}")
    public ResponseEntity<Object> getByIdUser(@PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC, page = 0, size = 5)  Pageable pageable,
                                              @PathVariable(value = "id") Long id){
        return threadService.getThreadByIdUser(id, pageable);
    }

    @ApiOperation(value = "Search thread by title", response = ThreadDtoResponse.class )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success Search thread by title"),
    })
    @GetMapping(value = "/search")
    public ResponseEntity<Object> searchThreadByTitle(@PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC, page = 0, size = 5)  Pageable pageable,
                                                      @RequestParam(value = "title", required = false) String title){
        return threadService.searchThreadByTitle(title, pageable);
    }

    @ApiOperation(value = "Search thread by category", response = ThreadDtoResponse.class )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success Search thread by category"),
    })
    @GetMapping(value = "")
    public ResponseEntity<Object> searchThreadByCategoryName(@PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC, page = 0, size = 5)  Pageable pageable,
                                                             @RequestParam(value = "category", required = false) String categoryName){
        return threadService.searchThreadByCategoryName(categoryName, pageable);
    }

    @ApiOperation(value = "Search trending thread", response = ThreadDtoResponse.class )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success Search trending thread"),
    })
    @GetMapping(value = "/trending")
    public ResponseEntity<Object> searchTrendingThread(@PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC, page = 0, size = 5)  Pageable pageable){
        return threadService.searchTrendingThread(pageable);
    }

    @ApiOperation(value = "Delete thread", response = ThreadDto.class )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success Delete thread"),
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deleteThread(@PathVariable(value = "id") Long id) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return threadService.deleteThread(id, (UserDao) userDetails);
    }

    @ApiOperation(value = "Update thread", response = ThreadDto.class )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success Update thread"),
    })
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> updateThread(@PathVariable(value = "id") Long id, @RequestParam("json") String request,
                                               @RequestParam(value = "file", required = false) MultipartFile multipartFile) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        ThreadDto threadDto = mapper.readValue(request, ThreadDto.class);

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return threadService.updateThread(id, threadDto, multipartFile, (UserDao) userDetails);
    }
}
