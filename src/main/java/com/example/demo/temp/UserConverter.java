package com.example.demo.temp;

import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    // Entity → Response
    public UserResponse toResponse(UserRds user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }

    // Request → Entity
    public UserRds toEntity(UserRequest request) {
        UserRds user = new UserRds();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        return user;
    }
}