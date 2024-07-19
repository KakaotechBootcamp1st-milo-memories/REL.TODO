package com.myapp.todo.dto;

import com.myapp.todo.entity.user.UserEntity;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class UserDTO implements Serializable {
    private final String name;
    private final String email;
    private final String picture;
    private final String provider;
    private final String providerId;

    public UserDTO(UserEntity userEntity) {
        this.name = userEntity.getName();
        this.email = userEntity.getEmail();
        this.picture = userEntity.getPicture();
        this.provider = userEntity.getProvider();
        this.providerId = userEntity.getProviderId();
    }
}
