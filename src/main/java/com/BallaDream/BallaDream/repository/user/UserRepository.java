package com.BallaDream.BallaDream.repository.user;

import com.BallaDream.BallaDream.domain.enums.LoginType;
import com.BallaDream.BallaDream.domain.user.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByUsername(String username);

    Boolean existsByUsernameAndLoginType(String username, LoginType loginType);

    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameAndLoginType(String username, LoginType loginType);

    @Modifying
//    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "update users set enabled = false, deleted_at = now() where user_id = :id", nativeQuery = true)
    void softDeleteUser(@Param("id") Long id);
}
