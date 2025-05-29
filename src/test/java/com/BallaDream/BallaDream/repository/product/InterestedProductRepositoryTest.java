package com.BallaDream.BallaDream.repository.product;

import com.BallaDream.BallaDream.domain.enums.DiagnoseType;
import com.BallaDream.BallaDream.domain.product.InterestedProduct;
import com.BallaDream.BallaDream.domain.product.Product;
import com.BallaDream.BallaDream.domain.user.User;
import com.BallaDream.BallaDream.repository.user.UserRepository;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class InterestedProductRepositoryTest {

    @Autowired UserRepository userRepository;
    @Autowired InterestedProductRepository interestedProductRepository;
    @Autowired ProductRepository productRepository;
    @Autowired EntityManager em;

    @Test
    @Transactional
    @DisplayName("사전에 관심 있는 상품으로 등록했는지 확인")
    void addProductTest() {

        //given
        User user = new User();
        userRepository.save(user);
        Product product = new Product(1000000L, "name", 100, "link1",
                "link12", "formulation");
        productRepository.save(product);
        InterestedProduct interestedProduct = new InterestedProduct(user, product);
        interestedProduct.associateProduct(product);
        interestedProduct.associateUser(user);

        //when
        interestedProductRepository.save(interestedProduct);

        //then
        InterestedProduct result = interestedProductRepository.findByUserAndDiagnoseType(user, DiagnoseType.ACNE).get();
        assertThat(result).isEqualTo(interestedProduct);
        assertThat(result.getProduct()).isEqualTo(product);
        assertThat(result.getUser()).isEqualTo(user);
    }

    @Test
    @Transactional
    @DisplayName("관심 등록 상품 해제하기")
    void deleteQueryTest() {

        //given
        User user = new User();
        userRepository.save(user);
        Product product = new Product(1000000L, "name", 100, "link1",
                "link12", "formulation");
        productRepository.save(product);
        InterestedProduct interestedProduct = new InterestedProduct(user, product);
        interestedProduct.associateProduct(product);
        interestedProduct.associateUser(user);
        interestedProductRepository.save(interestedProduct);

        //when
//        interestedProductRepository.deleteByUserAndDiagnoseType(user, DiagnoseType.ACNE);
        interestedProductRepository.deleteByUserAndProduct(user, product);

        //then
        Optional<InterestedProduct> result = interestedProductRepository.findById(interestedProduct.getId());
        assertThat(result.isEmpty()).isTrue();
    }
}