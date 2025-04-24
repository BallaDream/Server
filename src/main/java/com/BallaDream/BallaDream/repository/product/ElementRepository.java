package com.BallaDream.BallaDream.repository.product;

import com.BallaDream.BallaDream.domain.product.Element;
import com.BallaDream.BallaDream.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ElementRepository extends JpaRepository<Element, Long> {
}
