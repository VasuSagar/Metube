package com.itm.metube.controller;

import com.itm.metube.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;


    @PostMapping("/subscribe/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void subscribeUser(@PathVariable String userId) {
        userService.subscribeUser(userId);
    }

    @PostMapping("/unsubscribe/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void unsubscribe(@PathVariable String userId) {
        userService.unsubscribe(userId);
    }

    @PostMapping("/getAllSubscribedUser")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> getAllSubscribedUser() {
        return userService.getAllSubscribedUsers();
    }

    @GetMapping("/getAllHistoryVideos")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Map<String, Object>> getAllHistoryVideos(@RequestParam(required = false) String search) {
        return userService.getAllHistoryVideos(search);
    }




}
