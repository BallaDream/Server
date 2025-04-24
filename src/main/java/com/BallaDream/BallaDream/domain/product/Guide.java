package com.BallaDream.BallaDream.domain.product;

import com.BallaDream.BallaDream.domain.enums.DiagnosisType;
import com.BallaDream.BallaDream.domain.enums.Level;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Guide {
    @Id
    @Column(name = "guide_id")
    private Long id;

    private String description;

    @Column(name = "diagnosis_type")
    @Enumerated(EnumType.STRING)
    private DiagnosisType diagnosisType;

    @Enumerated(EnumType.STRING)
    private Level level;

    @OneToMany(mappedBy = "guide")
    private List<ProductGuide> productGuides = new ArrayList<>();

    public Guide() {
    }

    public Guide(Long id, String description, DiagnosisType diagnosisType, Level level) {
        this.id = id;
        this.description = description;
        this.diagnosisType = diagnosisType;
        this.level = level;
    }
}
