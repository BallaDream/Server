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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
class InterestedProductRepositoryTest {

    @Autowired UserRepository userRepository;
    @Autowired InterestedProductRepository interestedProductRepository;
    @Autowired ProductRepository productRepository;
    @Autowired EntityManager em;

    @Test
    @Transactional
    @DisplayName("관심 제품 등록하기")
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
        List<InterestedProduct> result = interestedProductRepository.findByUserId(user.getId());
        assertThat(result.size()).isEqualTo(1);
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
        interestedProductRepository.deleteByUserAndProduct(user, product);

        //then
        Optional<InterestedProduct> result = interestedProductRepository.findById(interestedProduct.getId());
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    @Transactional
    @DisplayName("관심 제품 페이지네이션해서 가져오기")
    void findInterestedProductsWithPaging() {

        //given
        User user = new User();
        userRepository.save(user);
        List<Product> products = getProducts();
        productRepository.saveAll(products);
        for (Product product : products) {
            InterestedProduct interestedProduct = new InterestedProduct(user, product);
            interestedProduct.associateProduct(product);
            interestedProduct.associateUser(user);
            interestedProductRepository.save(interestedProduct);
        }

        //when
        PageRequest pageRequest = PageRequest.of(0, 9);
        Page<InterestedProduct> content = interestedProductRepository.findByUserId(user.getId(), pageRequest);
        List<InterestedProduct> result = content.getContent();

        //then
        assertThat(result.size()).isEqualTo(9);
        assertThat(content.getTotalPages()).isEqualTo(4);
    }

    @Test
    @Transactional
    @DisplayName("고객 id와 제품 id로 관심 제품 찾기")
    void findInterestedProductWithUserIdAndProductId() {

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
        Optional<InterestedProduct> result = interestedProductRepository.findByUserIdAndProductId(user.getId(), product.getId());

        //then
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isEqualTo(interestedProduct);
    }

    //임의로 100개의 제품 만들기
    List<Product> getProducts() {
        List<Product> data = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            data.add(new Product(1000000L + i, "name", 100 + i, "link1",
                    "link12", "formulation"));
        };
        return data;
    }
}