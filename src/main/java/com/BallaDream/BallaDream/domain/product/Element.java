package com.BallaDream.BallaDream.domain.product;

import jakarta.persistence.*;

@Entity
public class Element {
    @Id
    @Column(name = "element_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_name")
    private Product product;

    @Column(name = "element_name")
    private String elementName;
}
