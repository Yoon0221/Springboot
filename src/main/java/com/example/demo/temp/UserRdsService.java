package com.example.demo.temp;

public interface UserRdsService {
    UserResponse getUserByUsername(String username); // 회원 이름으로 조회
    UserResponse addUser(UserRequest userRequest);  // 회원 추가
}