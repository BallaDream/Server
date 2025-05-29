package com.BallaDream.BallaDream.repository.product;

import com.BallaDream.BallaDream.domain.enums.DiagnoseType;
import com.BallaDream.BallaDream.domain.product.InterestedProduct;
import com.BallaDream.BallaDream.domain.product.Product;
import com.BallaDream.BallaDream.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;
import java.util.Optional;

public interface InterestedProductRepository extends JpaRepository<InterestedProduct, Long> {

    Optional<InterestedProduct> findByUserAndDiagnoseType(User user, DiagnoseType diagnoseType);

    Optional<InterestedProduct> findByUserAndProductAndDiagnoseType(User user, Product product, DiagnoseType diagnoseType);

    List<InterestedProduct> findByUser(User user);

    @Modifying
    void deleteByUserAndDiagnoseType(User user, DiagnoseType diagnoseType);

    @Modifying
    void deleteByUserAndDiagnoseTypeAndProduct(User user, DiagnoseType diagnoseType, Product product);

    @Modifying
    void deleteByUserAndProduct(User user, Product product);

}
