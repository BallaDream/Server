package com.BallaDream.BallaDream.service.product;

import com.BallaDream.BallaDream.domain.enums.DiagnoseType;
import com.BallaDream.BallaDream.domain.product.InterestedProduct;
import com.BallaDream.BallaDream.domain.product.Product;
import com.BallaDream.BallaDream.domain.user.User;
import com.BallaDream.BallaDream.exception.user.AlreadyInterestedProductException;
import com.BallaDream.BallaDream.exception.user.InvalidInputException;
import com.BallaDream.BallaDream.exception.product.ProductNotFoundException;
import com.BallaDream.BallaDream.exception.user.UserException;
import com.BallaDream.BallaDream.repository.product.InterestedProductRepository;
import com.BallaDream.BallaDream.repository.product.ProductGuideRepository;
import com.BallaDream.BallaDream.repository.product.ProductRepository;
import com.BallaDream.BallaDream.repository.product.query.ProductQueryRepository;
import com.BallaDream.BallaDream.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.BallaDream.BallaDream.constants.ResponseCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class InterestedProductService {

    private final InterestedProductRepository interestedProductRepository;
    private final ProductRepository productRepository;
    private final ProductGuideRepository productGuideRepository;
    private final ProductQueryRepository productQueryRepository;
    private final UserRepository userRepository;

    @Transactional
    public void addInterestedProduct(Long id, DiagnoseType diagnoseType, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UserException(INVALID_USER));


        //해당 상품이 존재하는지 확인
        Product product = productQueryRepository.findByIdAndDiagnoseType(id, diagnoseType);
        if (product == null) {
            throw new InvalidInputException(); //일치하지 않는 경우 예외 발생
        }

        //관심있는 상품이 이미 등록되어 있는지 확인
        Optional<InterestedProduct> interestedProductOpt = interestedProductRepository.
                findByUserAndProductAndDiagnoseType(user, product, diagnoseType);
        if (interestedProductOpt.isPresent()) {
            InterestedProduct data = interestedProductOpt.get();
            throw new AlreadyInterestedProductException();
        }

        //관심 제품 등록
        InterestedProduct interestedProduct = new InterestedProduct(user, product);
//        InterestedProduct interestedProduct = new InterestedProduct(user, diagnoseType, product);
        interestedProduct.associateProduct(product);
        interestedProduct.associateUser(user);
        interestedProductRepository.save(interestedProduct);
    }

    @Transactional
    public void deleteInterestedProduct(Long productId, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UserException(INVALID_USER));

        Product product = productRepository.findById(productId).orElseThrow(
                ProductNotFoundException::new);

        interestedProductRepository.deleteByUserAndProduct(user, product); //데이터 삭제
    }
}
