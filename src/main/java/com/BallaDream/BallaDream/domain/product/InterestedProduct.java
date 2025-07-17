package com.BallaDream.BallaDream.domain.product;

import com.BallaDream.BallaDream.domain.enums.DiagnoseType;
import com.BallaDream.BallaDream.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Table(name = "interested_product")
@Entity
public class InterestedProduct {
    @Id
    @Column(name = "interested_product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private LocalDate date; //관심 상품 등록 시기

    public InterestedProduct() {
    }

    public InterestedProduct(User user, Product product) {
        this.user = user;
        this.product = product;
        this.date = LocalDate.now();
    }

    //연관 관계 매핑 메서드
    public void associateUser(User user) {
        user.getInterestedProducts().add(this); //연관관계 매핑
    }

    public void associateProduct(Product product) {
        product.getInterestedProducts().add(this);
    }
}
