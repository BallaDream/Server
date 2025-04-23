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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Level level;

    @Column(name = "diagnosis_type")
    @Enumerated(EnumType.STRING)
    private DiagnosisType diagnosisType;

    private String description;

    @OneToMany(mappedBy = "guide")
    private List<ProductGuide> productGuides = new ArrayList<>();
}
