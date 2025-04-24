package com.BallaDream.BallaDream.repository.product;

import com.BallaDream.BallaDream.domain.product.Guide;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GuideRepository extends JpaRepository<Guide, Long> {
}
