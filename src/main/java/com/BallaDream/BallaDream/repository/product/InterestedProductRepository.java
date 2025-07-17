package com.BallaDream.BallaDream.repository.product;

import com.BallaDream.BallaDream.domain.diagnose.Diagnose;
import com.BallaDream.BallaDream.domain.enums.DiagnoseType;
import com.BallaDream.BallaDream.domain.product.InterestedProduct;
import com.BallaDream.BallaDream.domain.product.Product;
import com.BallaDream.BallaDream.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;
import java.util.Optional;

public interface InterestedProductRepository extends JpaRepository<InterestedProduct, Long> {

    List<InterestedProduct> findByUser(User user);

    List<InterestedProduct> findByUserId(Long userId);

    Optional<InterestedProduct> findByUserIdAndProductId(Long userId, Long ProductId);

    Optional<InterestedProduct> findByUserAndProduct(User user, Product product);

    Page<InterestedProduct> findByUserId(Long userId, Pageable pageable);

    @Modifying
    void deleteByUserAndProduct(User user, Product product);
}
