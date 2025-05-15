package com.BallaDream.BallaDream.repository.diagnose;

import com.BallaDream.BallaDream.domain.diagnose.Diagnose;
import com.BallaDream.BallaDream.domain.enums.Level;
import com.BallaDream.BallaDream.domain.user.User;
import jdk.jshell.Diag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DiagnoseRepository extends JpaRepository<Diagnose, Long> {
    List<Diagnose> findByUser(User user);

    boolean existsByUser(User user);

    Page<Diagnose> findByUser(User user, Pageable pageable);

    Optional<Diagnose> findByIdAndUserId(Long diagnoseId, Long userId);
}
