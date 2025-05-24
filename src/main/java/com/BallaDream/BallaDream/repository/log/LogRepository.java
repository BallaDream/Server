package com.BallaDream.BallaDream.repository.log;

import com.BallaDream.BallaDream.domain.log.SystemLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<SystemLog, Long> {
}
