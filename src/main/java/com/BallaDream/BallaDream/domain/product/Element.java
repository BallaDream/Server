package com.BallaDream.BallaDream.domain.product;

import jakarta.persistence.*;

@Entity
public class Element {
    @Id
    @Column(name = "element_id")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "element_name")
    private String elementName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;


    public Element() {
    }

    public Element(Long id, String elementName, Product product) {
        this.id = id;
        this.elementName = elementName;
        this.product = product;
    }
}
