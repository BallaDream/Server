package com.BallaDream.BallaDream.domain;

import com.BallaDream.BallaDream.constants.LoginType;
import com.BallaDream.BallaDream.constants.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Table(name = "users")
@Entity
@Setter
@Getter
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String username;
    private String password;

    @Column(name = "login_type")
    @Enumerated(EnumType.STRING)
    private LoginType loginType;

//    private String role;

    @Column(name = "user_role")
    @Enumerated(EnumType.STRING)
    private UserRole role;

    public UserEntity() {
    }

    //JWT 토큰에서 role을 가져왔을 때, 어느 역할에 해당하는지 확인하고 할당하는 생성자
    public UserEntity(String username, String role) {
        this.username = username;
        UserRole roleType = UserRole.valueOfRole(role);
        if (roleType == null) {
            throw new RuntimeException("토큰에 배정된 역할이 없음"); //Todo 예외 정의
        }
        this.role = roleType;
        this.password = "temp"; //context 저장을 위한 임시 비밀번호
    }

    public UserEntity(String username, UserRole role) {
        this.username = username;
//        this.role = role;
    }
}

