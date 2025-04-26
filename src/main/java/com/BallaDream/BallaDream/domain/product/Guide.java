package com.BallaDream.BallaDream.domain.product;

import com.BallaDream.BallaDream.domain.enums.DiagnoseType;
import com.BallaDream.BallaDream.domain.enums.Level;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
public class Guide {
    @Id
    @Column(name = "guide_id")
    private Long id;

    private String description;

    @Column(name = "diagnosis_type")
    @Enumerated(EnumType.STRING)
    private DiagnoseType diagnoseType;

    @Enumerated(EnumType.STRING)
    private Level level;

    @OneToMany(mappedBy = "guide")
    private List<ProductGuide> productGuides = new ArrayList<>();

    public Guide() {
    }

    public Guide(Long id, String description, DiagnoseType diagnoseType, Level level) {
        this.id = id;
        this.description = description;
        this.diagnoseType = diagnoseType;
        this.level = level;
    }
}
