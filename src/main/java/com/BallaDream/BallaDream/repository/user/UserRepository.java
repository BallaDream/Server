package com.BallaDream.BallaDream.repository.user;

import com.BallaDream.BallaDream.domain.enums.LoginType;
import com.BallaDream.BallaDream.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByUsername(String username);

    Boolean existsByUsernameAndLoginType(String username, LoginType loginType);

    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameAndLoginType(String username, LoginType loginType);
}
