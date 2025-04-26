package com.BallaDream.BallaDream.repository.product.query;

import com.BallaDream.BallaDream.domain.enums.DiagnosisType;
import com.BallaDream.BallaDream.domain.enums.Level;
import com.BallaDream.BallaDream.domain.user.User;
import com.BallaDream.BallaDream.dto.RecommendProductQueryDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

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

    @PersistenceContext EntityManager em;
    JPAQueryFactory queryFactory;

    @BeforeEach
    public void before() {
        queryFactory = new JPAQueryFactory(em);
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
                        .and(interestedProduct.diagnosisType.eq(DiagnosisType.DRY))) //Todo 추천 제품의 종류
                .where(
                        guide.level.eq(Level.CAUTION),
                        guide.diagnosisType.eq(DiagnosisType.DRY),
                        formulationEq("립밤"),
                        priceBetween(3000, 10000)
                )
                .fetch();

        log.info("{}", result.size());
        /*for (TempDto tempDto : result) {
            log.info("{} {}", tempDto.getProductName(), tempDto.getElementName());
        }*/
    }

    private BooleanExpression formulationEq(String formulationCond) {
        return formulationCond != null ? product.formulation.eq(formulationCond) : null;
    }
    private BooleanExpression priceBetween(Integer minPrice, Integer maxPrice) {
        return !(minPrice == null || maxPrice == null) ? product.price.between(minPrice, maxPrice) : null;
    }
}