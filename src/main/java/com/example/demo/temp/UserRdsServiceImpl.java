package com.example.demo.temp;

import com.example.demo.base.code.status.exception.ErrorStatus;
import com.example.demo.base.code.status.exception.GeneralException;
import org.springframework.stereotype.Service;

@Service
public class UserRdsServiceImpl implements UserRdsService {
    private final UserRdsRepository userRdsRepository;
    private final UserConverter userConverter;

    public UserRdsServiceImpl(UserRdsRepository userRdsRepository, UserConverter userConverter) {
        this.userRdsRepository = userRdsRepository;
        this.userConverter = userConverter;
    }

    // 에러 핸들러 -> 멤버가 없으면 TEMP_EXCEPTION 반환
    @Override
    public UserResponse getUserByUsername(String username) {
        UserRds user = userRdsRepository.findByUsername(username)
                .orElseThrow(() -> new GeneralException(ErrorStatus.TEMP_EXCEPTION.getReasonHttpStatus()));
        return userConverter.toResponse(user);
    }

    @Override
    public UserResponse addUser(UserRequest userRequest) {
        UserRds newUser = userConverter.toEntity(userRequest);
        UserRds savedUser = userRdsRepository.save(newUser);
        return userConverter.toResponse(savedUser);
    }
}