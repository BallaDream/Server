package com.BallaDream.BallaDream.service.log;

import com.BallaDream.BallaDream.domain.enums.Action;
import com.BallaDream.BallaDream.domain.log.SystemLog;
import com.BallaDream.BallaDream.repository.log.LogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class LogService {

    private final LogRepository logRepository;

    //사용자에 관련된 로그 기록 ex) 회원 탈퇴, 회원 탈퇴 후 재 회원가입
    public void recordUserLog(Long userId, String description, String module, Action action) {
        SystemLog log = new SystemLog(userId, LocalDate.now(), description, module, action);
        logRepository.save(log);
    }
}
