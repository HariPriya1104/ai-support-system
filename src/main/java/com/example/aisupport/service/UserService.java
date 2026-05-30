package com.example.aisupport.service;

import com.example.aisupport.model.Ticket;
import com.example.aisupport.model.User;
import com.example.aisupport.repository.UserRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public User createUser(User user)
    {
        return userRepository.save(user);
    }

    public List<User> getAllUser()
    {
        return userRepository.findAll();
    }

}
