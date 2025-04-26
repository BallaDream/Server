package com.BallaDream.BallaDream.repository.diagnose;

import com.BallaDream.BallaDream.domain.diagnose.UserSkinLevel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserSkinLevelRepository extends JpaRepository<UserSkinLevel, Long> {

    List<UserSkinLevel> findByDiagnoseId(Long diagnoseId);
}
