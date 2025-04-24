package com.BallaDream.BallaDream.domain.diagnose;

import com.BallaDream.BallaDream.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    private LocalDate date;
//    private String image; //Todo blob 형식으로 이미지 저장

    @OneToMany(mappedBy = "diagnose")
    private List<UserSkinLevel> skinLevelList = new ArrayList<>();

    public Diagnose() {
    }

    public Diagnose(User user) {
        this.user = user;
        user.getDiagnoses().add(this); //연관 관계
        date = LocalDate.now();
    }
}
