package com.example.rideshare.service;

import com.example.rideshare.dto.LoginRequest;
import com.example.rideshare.dto.RegisterRequest;
import com.example.rideshare.exception.BadRequestException;
import com.example.rideshare.model.User;
import com.example.rideshare.repository.UserRepository;
import com.example.rideshare.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public User register(RegisterRequest request){
        if(userRepository.findByUsername(request.getUsername()).isPresent()){
            throw new BadRequestException("Username is already taken");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        userRepository.save(user);
        return user;
    }

    @Override
    public String login(LoginRequest request) {
        Optional<User> user = userRepository.findByUsername(request.getUsername());
        if(user.isEmpty()){
            throw new BadRequestException("Username not found");
        }
        if(!passwordEncoder.matches(request.getPassword(),user.get().getPassword())){
            throw new BadRequestException("Wrong password");
        }

        return jwtUtil.generateToken(user.get().getUsername(), user.get().getRole());
    }

    public User loadUserByUsername(String username){
        Optional<User> user = userRepository.findByUsername(username);
    }

}
