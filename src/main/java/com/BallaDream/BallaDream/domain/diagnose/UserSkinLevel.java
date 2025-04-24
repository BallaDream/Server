package com.BallaDream.BallaDream.domain.diagnose;

import com.BallaDream.BallaDream.domain.enums.DiagnosisType;
import com.BallaDream.BallaDream.domain.enums.Level;
import jakarta.persistence.*;

@Table(name = "user_skin_level")
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

    @Column(name = "diagnosis_type")
    @Enumerated(EnumType.STRING)
    private DiagnosisType diagnosisType;

    public UserSkinLevel() {
    }

    public UserSkinLevel(Diagnose diagnose, Level level, DiagnosisType diagnosisType) {
        this.diagnose = diagnose;
        diagnose.getSkinLevelList().add(this); //연관 관계
        this.level = level;
        this.diagnosisType = diagnosisType;
    }
}
