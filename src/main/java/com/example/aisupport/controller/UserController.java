package com.example.aisupport.controller;

import com.example.aisupport.model.Ticket;
import com.example.aisupport.model.User;
import com.example.aisupport.repository.UserRepository;
import com.example.aisupport.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/user")
    public User createUser(@RequestBody User user)
    {
        return userService.createUser(user);
    }

    @GetMapping("/user")
    public List<User> getAllUser()
    {
        return userService.getAllUser();
    }
}
