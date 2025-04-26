package com.BallaDream.BallaDream.domain.product;

import jakarta.persistence.*;

@Table(name = "product_guide")
@Entity
public class ProductGuide {

    @Id
    @Column(name = "product_guide_id")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guide_id")
    private Guide guide;

    public ProductGuide() {
    }

    public ProductGuide(Long id, Product product, Guide guide) {
        this.id = id;
        this.product = product;
        this.guide = guide;
    }

    public void associateProduct(Product product) {
        product.getProductGuides().add(this);
    }

    public void associateGuide(Guide guide) {
        guide.getProductGuides().add(this);
    }
}
