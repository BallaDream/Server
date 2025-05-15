package com.BallaDream.BallaDream.domain.diagnose;

import com.BallaDream.BallaDream.domain.enums.DiagnoseType;
import com.BallaDream.BallaDream.domain.enums.Level;
import com.BallaDream.BallaDream.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.BallaDream.BallaDream.domain.enums.DiagnoseType.*;
import static com.BallaDream.BallaDream.domain.enums.Level.*;

@Getter
@Entity
@Builder
@AllArgsConstructor
public class Diagnose {
    @Id
    @Column(name = "diagnose_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDate date = LocalDate.now();

    //각 세부 부위별 진단 결과
    @Enumerated(EnumType.STRING)
    @Column(name = "dry_lips_level")
    private Level dryLipsLevel; //입술 건조도

    @Enumerated(EnumType.STRING)
    @Column(name = "pigment_forehead_level")
    private Level pigmentForeheadLevel; //이마 색소침착
    @Enumerated(EnumType.STRING)
    @Column(name = "pigment_left_cheek_level")
    private Level pigmentLeftCheekLevel; //왼쪽볼 색소침착
    @Enumerated(EnumType.STRING)
    @Column(name = "pigment_right_cheek_level")
    private Level pigmentRightCheekLevel; //오른쪽볼 색소침착

    @Enumerated(EnumType.STRING)
    @Column(name = "wrinkle_forehead_level")
    private Level wrinkleForeheadLevel; //이마 주름
    @Enumerated(EnumType.STRING)
    @Column(name = "wrinkle_glabella_level")
    private Level wrinkleGlabellaLevel; //미간 주름
    @Enumerated(EnumType.STRING)
    @Column(name = "wrinkle_left_eye_level")
    private Level wrinkleLeftEyeLevel; //왼쪽 눈가 주름
    @Enumerated(EnumType.STRING)
    @Column(name = "wrinkle_right_eye_level")
    private Level wrinkleRightEyeLevel; //오른쪽 눈가 주름

    @Enumerated(EnumType.STRING)
    @Column(name = "pore_left_cheek_level")
    private Level poreLeftCheekLevel; //왼쪽 볼 모공
    @Enumerated(EnumType.STRING)
    @Column(name = "pore_right_cheek_level")
    private Level poreRightCheekLevel; //오른쪽 볼 모공

    @Enumerated(EnumType.STRING)
    @Column(name = "elastic_jawline_sagging_level")
    private Level elasticJawlineSaggingLevel; //턱선 처짐

    @OneToMany(mappedBy = "diagnose")
    private List<UserSkinLevel> skinLevelList = new ArrayList<>();

    public Diagnose() {
    }

    public void createDate() {
        this.date = LocalDate.now();
    }

    public void associateUser(User user) {
        this.user = user;
        user.getDiagnoses().add(this); //연관 관계
    }

    //진단 세부 정보를 반환
    public Map<String, Level> getSpecificUserSkinLevel() {
        Map<String, Level> result = new HashMap<>();
        result.put("dryLipsLevel", dryLipsLevel);
        result.put("pigmentForeheadLevel", pigmentForeheadLevel);
        result.put("pigmentLeftCheekLevel", pigmentLeftCheekLevel);
        result.put("pigmentRightCheekLevel", pigmentRightCheekLevel);
        result.put("wrinkleForeheadLevel", wrinkleForeheadLevel);
        result.put("wrinkleGlabellaLevel", wrinkleGlabellaLevel);
        result.put("wrinkleLeftEyeLevel", wrinkleLeftEyeLevel);
        result.put("wrinkleRightEyeLevel", wrinkleRightEyeLevel);
        result.put("elasticJawlineSaggingLevel", elasticJawlineSaggingLevel);
        result.put("poreLeftCheekLevel", poreLeftCheekLevel);
        result.put("poreRightCheekLevel", poreRightCheekLevel);
        return result;
    }

    //진단 집계 결과를 반환
    public Map<DiagnoseType, Level> getTotalUserSkinLevel() {
        Map<DiagnoseType, Level> result = new HashMap<>();
        //입술 건조도 진단 결과
        result.put(DRY, dryLipsLevel);
        //색소 침착 진단 결과
        if (pigmentForeheadLevel == WARNING | pigmentLeftCheekLevel == WARNING | pigmentRightCheekLevel == WARNING) {
            result.put(PIGMENT, WARNING);
        } else if (pigmentForeheadLevel == CAUTION | pigmentLeftCheekLevel == CAUTION | pigmentRightCheekLevel == CAUTION) {
            result.put(PIGMENT, CAUTION);
        } else{
            result.put(PIGMENT, CLEAR);
        }
        //주름 진단 결과
        if (wrinkleForeheadLevel == WARNING | wrinkleGlabellaLevel == WARNING |
                wrinkleLeftEyeLevel == WARNING | wrinkleRightEyeLevel == WARNING) {

            result.put(WRINKLE, WARNING);

        } else if (wrinkleForeheadLevel == CAUTION | wrinkleGlabellaLevel == CAUTION |
                wrinkleLeftEyeLevel == CAUTION | wrinkleRightEyeLevel == CAUTION) {

            result.put(WRINKLE, CAUTION);

        } else{
            result.put(WRINKLE, CLEAR);
        }
        //탄력 진단 결과
        if (elasticJawlineSaggingLevel == WARNING) {
            result.put(ELASTIC, WARNING);
        } else if (elasticJawlineSaggingLevel == CAUTION) {
            result.put(ELASTIC, CAUTION);
        } else{
            result.put(ELASTIC, CLEAR);
        }
        //모공 진단 결과
        if (poreLeftCheekLevel == WARNING | poreRightCheekLevel == WARNING) {
            result.put(PORE, WARNING);
        } else if (poreLeftCheekLevel == CAUTION | poreRightCheekLevel == CAUTION) {
            result.put(PORE, CAUTION);
        } else {
            result.put(PORE, CLEAR);
        }

        return result;
    }
}
