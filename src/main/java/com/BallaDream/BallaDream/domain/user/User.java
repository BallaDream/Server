package com.BallaDream.BallaDream.domain.user;

import com.BallaDream.BallaDream.constants.ResponseCode;
import com.BallaDream.BallaDream.domain.diagnose.Diagnose;
import com.BallaDream.BallaDream.domain.enums.LoginType;
import com.BallaDream.BallaDream.domain.enums.UserRole;
import com.BallaDream.BallaDream.domain.product.InterestedProduct;
import com.BallaDream.BallaDream.exception.user.UserException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.net.UnknownServiceException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Table(name = "users")
@Entity
@Setter
@Getter
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String nickname;

    @Column(name = "login_type")
    @Enumerated(EnumType.STRING)
    private LoginType loginType;

    @Column(name = "user_role")
    @Enumerated(EnumType.STRING)
    private UserRole role;
    
    private boolean enabled; //계정 활성화 or 탈퇴
    
    @Column(name = "deleted_at")
    private LocalDate deletedAt; //계정 탈퇴 시각

    @OneToMany(mappedBy = "user")
    private List<InterestedProduct> interestedProducts = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Diagnose> diagnoses = new ArrayList<>();

    public User() {
    }

    //JWT 토큰에서 role을 가져왔을 때, 어느 역할에 해당하는지 확인하고 할당하는 생성자
    public User(String username, String role) {
        this.username = username;
        UserRole roleType = UserRole.valueOfRole(role);
        if (roleType == null) {
            throw new UserException(ResponseCode.INVALID_USER);
        }
        this.role = roleType;
        this.password = "temp"; //context 저장을 위한 임시 비밀번호
    }

    //최초의 회원가입을 했을때 아래 생성자를 사용한다.
    public User(String username, String password, LoginType loginType, UserRole role) {
        this.username = username;
        this.password = password;
        this.nickname = "dream"; //임시 닉네임
        this.loginType = loginType;
        this.role = role;
        this.enabled = true; //계정 활성화
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    public void makeEnable() {
        this.enabled = true;
    }
}

