package com.BallaDream.BallaDream.repository.product.query;

import com.BallaDream.BallaDream.domain.enums.DiagnoseType;
import com.BallaDream.BallaDream.domain.enums.Level;
import com.BallaDream.BallaDream.domain.product.Product;
import com.BallaDream.BallaDream.dto.product.RecommendProductQueryContent;
import com.BallaDream.BallaDream.dto.product.RecommendProductQueryDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.BallaDream.BallaDream.domain.product.QElement.element;
import static com.BallaDream.BallaDream.domain.product.QGuide.guide;
import static com.BallaDream.BallaDream.domain.product.QInterestedProduct.interestedProduct;
import static com.BallaDream.BallaDream.domain.product.QProduct.product;
import static com.BallaDream.BallaDream.domain.product.QProductGuide.productGuide;

@Slf4j
@RequiredArgsConstructor
@Repository
public class ProductQueryRepository {

    private final JPAQueryFactory queryFactory;

    private List<Long> getProductIds(DiagnoseType diagnoseType, Level level,String formulation, Integer minPrice,
                                     Integer maxPrice, int offset, int limit) {
        return queryFactory
                .select(product.id)
                .from(product)
                .join(productGuide).on(product.id.eq(productGuide.product.id))
                .join(guide).on(productGuide.guide.id.eq(guide.id))
                .where(
                        guide.level.eq(level),
                        guide.diagnoseType.eq(diagnoseType),
                        formulationEq(formulation),
                        priceBetween(minPrice, maxPrice)
                )
                .offset(offset)
                .limit(limit + 1) //페이지 네이션
                .orderBy(product.id.asc()) //정렬 조건 추가
                .fetch();
    }

    //추천 상품에 대한 정보를 반환
    public RecommendProductQueryDto recommendProduct(Long userId, DiagnoseType diagnoseType, Level level,
                                                     String formulation, Integer minPrice, Integer maxPrice,
                                                     int offset, int limit) {

        List<Long> productIds = getProductIds(diagnoseType, level, formulation, minPrice, maxPrice, offset, limit);
        boolean hasNextPage = productIds.size() > limit; //다음 페이지가 있는지 확인

        if (hasNextPage) {
            productIds = productIds.subList(0, limit); //중복 방지용으로 초과분 제거
        }

        List<RecommendProductQueryContent> content = queryFactory
                .select(Projections.constructor(RecommendProductQueryContent.class,
                        product.id,
                        product.productName,
                        product.formulation,
                        product.price,
                        product.salesLink,
                        product.imageLink,
                        element.elementName,
                        interestedProduct.id.isNotNull()
                ))
                .from(product)
                .join(element).on(product.id.eq(element.product.id))
                .leftJoin(interestedProduct).on(product.id.eq(interestedProduct.product.id))
                .where(product.id.in(productIds))
                .orderBy(product.id.asc()) //정렬 조건 추가
                .fetch();

        return new RecommendProductQueryDto(hasNextPage, content);
    }

    public Product findByIdAndDiagnoseType(Long id, DiagnoseType diagnoseType) {
        return queryFactory
                .select(product)
                .from(product)
                .join(productGuide).on(product.id.eq(productGuide.product.id))
                .join(guide).on(productGuide.guide.id.eq(guide.id))
                .where(product.id.eq(id).and(guide.diagnoseType.eq(diagnoseType)))
                .fetchOne();
    }

    //제형에 대한 검색
    private BooleanExpression formulationEq(String formulationCond) {
        return formulationCond != null ? product.formulation.eq(formulationCond) : null;
    }
    //가격에 대한 검색
    private BooleanExpression priceBetween(Integer minPrice, Integer maxPrice) {
        if (minPrice == null && maxPrice == null) {
            return null;
        } else if (minPrice == null) {
            return product.price.loe(maxPrice);
        } else if (maxPrice == null) {
            return product.price.goe(minPrice);
        }
        return product.price.between(minPrice, maxPrice);
    }
}
