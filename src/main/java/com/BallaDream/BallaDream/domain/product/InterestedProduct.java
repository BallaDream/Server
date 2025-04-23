package com.BallaDream.BallaDream.domain.product;

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

    private String productName;

    private String type; //Todo enum or String 생각할 것
}
