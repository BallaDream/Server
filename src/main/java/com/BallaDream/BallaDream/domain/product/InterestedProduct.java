package com.BallaDream.BallaDream.domain.product;

import com.BallaDream.BallaDream.domain.enums.DiagnoseType;
import com.BallaDream.BallaDream.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;

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

    @Column(name = "diagnose_type")
    @Enumerated(EnumType.STRING)
    private DiagnoseType diagnoseType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    public InterestedProduct() {
    }

    public InterestedProduct(User user, Product product) {
        this.user = user;
        this.diagnoseType = diagnoseType;
        this.product = product;
    }

//    public InterestedProduct(User user, DiagnoseType diagnoseType, Product product) {
//        this.user = user;
//        this.diagnoseType = diagnoseType;
//        this.product = product;
//    }

    //연관 관계 매핑 메서드
    public void associateUser(User user) {
        user.getInterestedProducts().add(this); //연관관계 매핑
    }

    public void associateProduct(Product product) {
        product.getInterestedProducts().add(this);
    }
}
