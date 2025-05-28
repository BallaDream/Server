package com.BallaDream.BallaDream.repository.product.query;

import com.BallaDream.BallaDream.domain.enums.DiagnoseType;
import com.BallaDream.BallaDream.domain.enums.Level;
import com.BallaDream.BallaDream.domain.product.Guide;
import com.BallaDream.BallaDream.domain.product.Product;
import com.BallaDream.BallaDream.domain.product.ProductGuide;
import com.BallaDream.BallaDream.domain.user.User;
import com.BallaDream.BallaDream.dto.product.RecommendProductQueryDto;
import com.BallaDream.BallaDream.repository.product.GuideRepository;
import com.BallaDream.BallaDream.repository.product.ProductGuideRepository;
import com.BallaDream.BallaDream.repository.product.ProductRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.BallaDream.BallaDream.domain.product.QElement.*;
import static com.BallaDream.BallaDream.domain.product.QGuide.*;
import static com.BallaDream.BallaDream.domain.product.QInterestedProduct.interestedProduct;
import static com.BallaDream.BallaDream.domain.product.QProduct.*;
import static com.BallaDream.BallaDream.domain.product.QProductGuide.*;
import static com.BallaDream.BallaDream.domain.user.QUser.*;
import static org.assertj.core.api.Assertions.*;


@Slf4j
@SpringBootTest
class ProductQueryRepositoryTest {

    @Autowired ProductQueryRepository productQueryRepository;
    @Autowired ProductRepository productRepository;
    @Autowired GuideRepository guideRepository;
    @Autowired ProductGuideRepository productGuideRepository;
    @PersistenceContext EntityManager em;
    JPAQueryFactory queryFactory;

    @BeforeEach
    public void before() {
        queryFactory = new JPAQueryFactory(em);
    }

    @Test
    @Transactional
    void findByIdAndDiagnoseTypeTest() {

        //given
        Product product = new Product(10000L, "p1", 1000, "link1", "link2", "formulation");
        Guide guide = new Guide(1000L, "desc", DiagnoseType.ACNE, Level.CLEAR);
        ProductGuide productGuide = new ProductGuide(100000L, product, guide);

        //when
        productRepository.save(product);
        guideRepository.save(guide);
        productGuide.associateGuide(guide);
        productGuide.associateProduct(product);
        productGuideRepository.save(productGuide);
        //then
        Product findResult = productQueryRepository.findByIdAndDiagnoseType(10000L, DiagnoseType.ACNE);
        Product nullResult = productQueryRepository.findByIdAndDiagnoseType(100000000L, DiagnoseType.ACNE); //없는 데이터
        assertThat(findResult.getId()).isEqualTo(product.getId());
        assertThat(nullResult).isNull();
    }

    @Test
    @DisplayName("가격으로 조회하기")
    void findProductByPrice() {

        //given


        //when

        //then

    }

    @Test
    void queryBasic() {
        User findUser = queryFactory
                .select(user)
                .from(user)
                .where(user.username.eq("dnjswo410@naver.com"))
                .fetchOne();

        assertThat(findUser.getUsername()).isEqualTo("dnjswo410@naver.com");
    }

    @Test
    void productJoinElement() {
        List<RecommendProductQueryDto> result = queryFactory
                .select(Projections.constructor(RecommendProductQueryDto.class,
                        product.productName,
                        product.formulation,
                        product.price,
                        product.salesLink,
                        product.imageLink,
                        element.elementName,
                        interestedProduct.id.isNotNull() // 관심 여부 boolean 처리
                ))
                .from(product)
                .join(productGuide).on(product.id.eq(productGuide.product.id))
                .join(element).on(product.id.eq(element.product.id))
                .join(guide).on(productGuide.guide.id.eq(guide.id))
                .leftJoin(interestedProduct)
                .on(product.id.eq(interestedProduct.product.id)
                        .and(interestedProduct.user.id.eq(3L)) //Todo 실제 사용자 pk
                        .and(interestedProduct.diagnoseType.eq(DiagnoseType.DRY))) //Todo 추천 제품의 종류
                .where(
                        guide.level.eq(Level.CAUTION),
                        guide.diagnoseType.eq(DiagnoseType.DRY),
                        formulationEq("립밤"),
                        priceBetween(3000, 10000)
                )
                .fetch();

        log.info("{}", result.size());
    }

    private BooleanExpression formulationEq(String formulationCond) {
        return formulationCond != null ? product.formulation.eq(formulationCond) : null;
    }
    private BooleanExpression priceBetween(Integer minPrice, Integer maxPrice) {
        return !(minPrice == null || maxPrice == null) ? product.price.between(minPrice, maxPrice) : null;
    }
}