package com.myapp.todo.service;

import com.myapp.todo.dto.UserDTO;
import com.myapp.todo.entity.user.UserEntity;
import com.myapp.todo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final String AUTHORIZATION = "Authorization";
    private final String BEARER_PREFIX = "Bearer ";
    private final String logoutURL = "https://kapi.kakao.com/v1/user/logout";

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public UserEntity updateName(Long userId, String newName) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid userId:" + userId));

        return user.update(newName, user.getPicture());
    }
}

