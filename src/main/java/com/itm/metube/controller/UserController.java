package com.itm.metube.controller;

import com.itm.metube.model.User;
import com.itm.metube.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/add")
    public String saveUser(@RequestBody User user){
        userRepository.save(user);
        return "USER";
    }

    @GetMapping("/get")
    public List<User> getUser(){
        return userRepository.findAll();
    }

}
