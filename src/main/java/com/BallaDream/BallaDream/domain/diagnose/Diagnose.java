package com.BallaDream.BallaDream.domain.diagnose;

import com.BallaDream.BallaDream.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
public class Diagnose {
    @Id
    @Column(name = "diagnose_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime date;
    private String image; //Todo blob 형식으로 이미지 저장

    @OneToMany(mappedBy = "diagnose")
    private List<UserSkinLevel> skinLevelList = new ArrayList<>();
}
