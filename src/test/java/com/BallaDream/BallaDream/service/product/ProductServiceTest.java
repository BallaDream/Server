package com.BallaDream.BallaDream.service.product;

import com.BallaDream.BallaDream.domain.enums.DiagnoseType;
import com.BallaDream.BallaDream.domain.product.Product;
import com.BallaDream.BallaDream.repository.product.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class ProductServiceTest {

    @Autowired ProductService productService;
    @Autowired ProductRepository productRepository;

    @Test
    @Transactional
    @DisplayName("상품의 진단 타입을 가져오기")
    void getProductDiagnoseType() {

        //given
        Product product = productRepository.findById(1L).get();

        //when
        List<DiagnoseType> result = productService.getProductDiagnoseType(product);

        //then
        assertThat(result.size()).isEqualTo(5); //1번 제품은 5개의 제품 타입을 갖추고 있음
    }
}