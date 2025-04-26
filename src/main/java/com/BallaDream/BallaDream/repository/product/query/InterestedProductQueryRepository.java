package com.BallaDream.BallaDream.repository.product.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@RequiredArgsConstructor
@Repository
public class InterestedProductQueryRepository {

    private final JPAQueryFactory queryFactory;

}
