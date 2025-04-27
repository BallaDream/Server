package com.BallaDream.BallaDream.service.product;

import com.BallaDream.BallaDream.constants.ResponseCode;
import com.BallaDream.BallaDream.domain.enums.DiagnoseType;
import com.BallaDream.BallaDream.domain.product.InterestedProduct;
import com.BallaDream.BallaDream.domain.product.Product;
import com.BallaDream.BallaDream.domain.user.User;
import com.BallaDream.BallaDream.exception.user.AlreadyInterestedProductException;
import com.BallaDream.BallaDream.exception.user.InvalidInputException;
import com.BallaDream.BallaDream.exception.user.UserException;
import com.BallaDream.BallaDream.repository.product.InterestedProductRepository;
import com.BallaDream.BallaDream.repository.product.ProductGuideRepository;
import com.BallaDream.BallaDream.repository.product.query.ProductQueryRepository;
import com.BallaDream.BallaDream.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class InterestedProductService {

    private final InterestedProductRepository productRepository;
    private final ProductGuideRepository productGuideRepository;
    private final ProductQueryRepository productQueryRepository;
    private final UserRepository userRepository;

    //Todo 하나 이상의 제품을 등록할 수 없는 버그가 생김
    public void addInterestedProduct(Long id, DiagnoseType diagnoseType, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UserException(ResponseCode.INVALID_USER));


        //상품과 그 상품의 피부 진단 종류가 일치하는지 확인
        Product product = productQueryRepository.findByIdAndDiagnoseType(id, diagnoseType);
        if (product == null) {
            throw new InvalidInputException(); //일치하지 않는 경우 예외 발생
        }

        //관심있는 상품이 이미 등록되어 있는지 확인
        Optional<InterestedProduct> interestedProductOpt = productRepository.
                findByUserAndProductAndDiagnoseType(user, product, diagnoseType);
        if (interestedProductOpt.isPresent()) {
            InterestedProduct data = interestedProductOpt.get();
            log.info("bug fix: {} {} {}", data.getUser().getId(), data.getProduct().getId(), data.getDiagnoseType());
            throw new AlreadyInterestedProductException();
        }

        //관심 제품 등록
        InterestedProduct interestedProduct = new InterestedProduct(user, diagnoseType, product);
        interestedProduct.associateProduct(product);
        interestedProduct.associateUser(user);
        productRepository.save(interestedProduct);
    }

    @Transactional
    public void deleteInterestedProduct(Long id, DiagnoseType diagnoseType, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UserException(ResponseCode.INVALID_USER));

        productRepository.deleteByUserAndDiagnoseType(user, diagnoseType); //데이터 삭제
    }
}
