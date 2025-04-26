package com.BallaDream.BallaDream.repository.diagnose;

import com.BallaDream.BallaDream.domain.diagnose.Diagnose;
import com.BallaDream.BallaDream.domain.enums.Level;
import com.BallaDream.BallaDream.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiagnoseRepository extends JpaRepository<Diagnose, Long> {
    List<Diagnose> findByUser(User user);

    boolean existsByUser(User user);
}
