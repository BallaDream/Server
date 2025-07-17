package com.BallaDream.BallaDream.domain.log;

import com.BallaDream.BallaDream.domain.enums.Action;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Entity
@Table(name = "system_log")
public class SystemLog {
    @Id
    @Column(name = "system_log_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "created_at")
    private LocalDate createdAt; //로그 발생 시각

    private String description; //상세 메시지

    private String module; //로그 발생 모듈

    @Enumerated(EnumType.STRING)
    private Action action; //수행한 액션명 ex) 회원탈퇴..

    public SystemLog() {
    }

    //사용자 관련 로그가 발생하는 경우
    public SystemLog(Long userId, String description, String module, Action action) {
        this.userId = userId;
        this.createdAt = LocalDate.now();
        this.description = description;
        this.module = module;
        this.action = action;
    }
}
