package com.BallaDream.BallaDream.constants;

import com.BallaDream.BallaDream.domain.enums.Level;
import org.apache.commons.beanutils.PropertyUtilsBean;

import static com.BallaDream.BallaDream.domain.enums.Level.*;

public enum DiagnoseScore {

    DRY_LIPS_SCORE(4, 2), //입술 건조도
    PIGMENT_FOREHEAD_SCORE(4, 2), //이마 색소 침착
    PIGMENT_CHEEK_SCORE(5, 3), //볼(왼쪽, 오른쪽) 색소 침착
    WRINKLE_FOREHEAD_SCORE(5, 2), //이마 주름
    WRINKLE_GLABELLA_SCORE(4, 2), //미간 주름
    WRINKLE_EYE_SCORE(4, 2), //눈가(왼쪽, 오른쪽) 주름
    PORE_CHEEK_SCORE(4, 2), //볼(왼쪽, 오른쪽) 색소 침착
    ELASTIC_JAWLINE_SAGGING_SCORE(4, 2); //턱선 처짐

    private final int warningScore;
    private final int cautionScore;

    DiagnoseScore(int warningScore, int cautionScore) {
        this.warningScore = warningScore;
        this.cautionScore = cautionScore;
    }

    //피부 진단 점수에 따른 진단 결과
    public Level getLevelByScore(int diagnoseScore) {
        if (diagnoseScore >= warningScore) {
            return WARNING;
        } else if (diagnoseScore >= cautionScore) {
            return CAUTION;
        } else {
            return CLEAR;
        }
    }

    private int getWarningScore() {
        return warningScore;
    }

    private int getCautionScore() {
        return cautionScore;
    }
}
