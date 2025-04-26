package com.BallaDream.BallaDream.domain.diagnose;

import com.BallaDream.BallaDream.domain.enums.DiagnoseType;
import com.BallaDream.BallaDream.domain.enums.Level;
import jakarta.persistence.*;
import lombok.Getter;

@Table(name = "user_skin_level")
@Getter
@Entity
public class UserSkinLevel {

    @Id
    @Column(name = "user_skin_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diagnose_id")
    private Diagnose diagnose;

    @Enumerated(EnumType.STRING)
    private Level level;

    @Column(name = "diagnose_type")
    @Enumerated(EnumType.STRING)
    private DiagnoseType diagnoseType;

    public UserSkinLevel() {
    }

    public UserSkinLevel(Diagnose diagnose, Level level, DiagnoseType diagnoseType) {
        this.diagnose = diagnose;
        this.level = level;
        this.diagnoseType = diagnoseType;
    }

    public void associateDiagnose(Diagnose diagnose) {
        diagnose.getSkinLevelList().add(this); //연관 관계
    }
}
