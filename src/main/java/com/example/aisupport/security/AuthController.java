package com.example.aisupport.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest)
    {
        if(loginRequest.getUserName().equals("admin") && loginRequest.getPassword().equals("password1123"))
        {
            return jwtUtil.generateToken(loginRequest.getUserName());
        }
        throw new RuntimeException("Invalid credentials");
    }
}
