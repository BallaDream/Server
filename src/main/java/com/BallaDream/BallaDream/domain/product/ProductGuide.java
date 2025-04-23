package com.BallaDream.BallaDream.domain.product;

import jakarta.persistence.*;

@Table(name = "product_guide")
@Entity
public class ProductGuide {

    @Id
    @Column(name = "product_guide_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_name")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guide_id")
    private Guide guide;
}
