package com.example.demo.temp;

import com.example.demo.base.code.status.exception.GeneralException;
import com.example.demo.base.code.status.exception.ErrorStatus;
import org.springframework.stereotype.Service;

@Service
public class TempQueryServiceImpl implements TempQueryService {

    @Override
    public void checkFlag(Integer flag) {
        if (flag == 1) {
            // ErrorStatus.TEMP_EXCEPTION.getReasonHttpStatus()를 사용하여 ErrorReasonDTO를 반환
            throw new GeneralException(ErrorStatus.TEMP_EXCEPTION.getReasonHttpStatus());
        }
    }
}