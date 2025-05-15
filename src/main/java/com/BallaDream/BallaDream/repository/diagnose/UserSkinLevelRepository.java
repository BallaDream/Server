package com.BallaDream.BallaDream.repository.diagnose;

import com.BallaDream.BallaDream.domain.diagnose.Diagnose;
import com.BallaDream.BallaDream.domain.diagnose.UserSkinLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserSkinLevelRepository extends JpaRepository<UserSkinLevel, Long> {

    List<UserSkinLevel> findByDiagnoseId(Long id);

//    @Query("select usl from UserSkinLevel usl left join fetch usl.diagnose")
//    List<UserSkinLevel>
}
