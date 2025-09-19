package com.BallaDream.BallaDream.repository.diagnose.query;

import com.BallaDream.BallaDream.domain.diagnose.Diagnose;
import com.BallaDream.BallaDream.dto.diagnose.UserAllDiagnoseQueryDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.BallaDream.BallaDream.domain.diagnose.QDiagnose.*;
import static com.BallaDream.BallaDream.domain.diagnose.QUserSkinLevel.*;

@Slf4j
@RequiredArgsConstructor
@Repository

public class DiagnoseQueryRepository {

    private final JPAQueryFactory queryFactory;

    //가장 최근의 진단 기록 가져오기
    public Diagnose findLatestDiagnoseByUser(Long userId) {
        return queryFactory
                .selectFrom(diagnose)
                .where(diagnose.user.id.eq(userId))
                .orderBy(diagnose.date.desc())
                .limit(1)
                .fetchOne();
    }

    //사용자의 모든 진단 기록 가져오기
    public List<UserAllDiagnoseQueryDto> findAllDiagnoseByUser(Long userId) {
        return queryFactory
                .select(Projections.constructor(UserAllDiagnoseQueryDto.class,
                        diagnose.id,
                        diagnose.date,
                        userSkinLevel.level,
                        userSkinLevel.diagnoseType))
                .from(userSkinLevel)
                .join(userSkinLevel.diagnose, diagnose)
                .where(diagnose.user.id.eq(userId))
                .fetch();
    }
}
