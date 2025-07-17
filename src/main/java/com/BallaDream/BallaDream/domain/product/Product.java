package com.BallaDream.BallaDream.domain.product;

import com.BallaDream.BallaDream.domain.enums.DiagnoseType;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Entity
public class Product {

    @Id
    @Column(name = "product_id")
    private Long id;

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

    @OneToMany(mappedBy = "product")
    private List<InterestedProduct> interestedProducts = new ArrayList<>();

    public Product() {
    }

    public Product(Long id, String productName, int price, String salesLink, String imageLink, String formulation) {
        this.id = id;
        this.productName = productName;
        this.price = price;
        this.salesLink = salesLink;
        this.imageLink = imageLink;
        this.formulation = formulation;
    }

    //해당 제품의 진단 종류를 모두 가져오기
    public List<DiagnoseType> getProductDiagnoseType() {
        return this.getProductGuides().stream()
                .map(ProductGuide::getGuide)
                .map(Guide::getDiagnoseType)
                .collect(Collectors.toList());
    }

    //해당 제품의 성분을 모두 가져오기
    public List<String> getProductElements() {
        return this.getElements().stream()
                .map(Element::getElementName)
                .collect(Collectors.toList());
    }
}
