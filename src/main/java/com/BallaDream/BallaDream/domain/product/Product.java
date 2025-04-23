package com.BallaDream.BallaDream.domain.product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
public class Product {

    @Id
    @Column(name = "product_name")
    private String productName;
    private int price;

    @Column(name = "sales_link")
    private String salesLink;

    @Column(name = "image_link")
    private String imageLink;

    private String formulation;

    @OneToMany(mappedBy = "product")
    private List<ProductGuide> productGuides = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private List<Element> elements = new ArrayList<>();
}
