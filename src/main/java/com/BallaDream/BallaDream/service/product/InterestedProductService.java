package com.BallaDream.BallaDream.service.product;

import com.BallaDream.BallaDream.domain.enums.DiagnoseType;
import com.BallaDream.BallaDream.domain.product.InterestedProduct;
import com.BallaDream.BallaDream.domain.product.Product;
import com.BallaDream.BallaDream.domain.user.User;
import com.BallaDream.BallaDream.dto.mypage.MyPageInterestedProductDto;
import com.BallaDream.BallaDream.dto.mypage.MyPageInterestedProductResponseDto;
import com.BallaDream.BallaDream.exception.user.AlreadyInterestedProductException;
import com.BallaDream.BallaDream.exception.product.ProductNotFoundException;
import com.BallaDream.BallaDream.exception.user.UserException;
import com.BallaDream.BallaDream.repository.product.InterestedProductRepository;
import com.BallaDream.BallaDream.repository.product.ProductRepository;
import com.BallaDream.BallaDream.repository.product.query.ProductQueryRepository;
import com.BallaDream.BallaDream.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.BallaDream.BallaDream.constants.ResponseCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class InterestedProductService {

    private final InterestedProductRepository interestedProductRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;
    private final ProductQueryRepository productQueryRepository;
    private final UserRepository userRepository;

    //관심 제품 등록
    @Transactional
    public void addInterestedProduct(Long productId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserException(INVALID_USER));

        //올바른 상품인지 조회
        Product product = productRepository.findById(productId).orElseThrow(
                ProductNotFoundException::new);

        //관심있는 상품이 이미 등록되어 있는지 확인
        Optional<InterestedProduct> alreadyInterestedProduct = interestedProductRepository.findByUserIdAndProductId(userId, productId);
        if (alreadyInterestedProduct.isPresent()) {
            throw new AlreadyInterestedProductException(); //일치하지 않는 경우 예외 발생
        }

        //관심 제품 등록
        InterestedProduct interestedProduct = new InterestedProduct(user, product);
        interestedProduct.associateProduct(product);
        interestedProduct.associateUser(user);
        interestedProductRepository.save(interestedProduct);
    }

    //관심 제품 등록 해제
    @Transactional
    public void deleteInterestedProduct(Long productId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserException(INVALID_USER));

        Product product = productRepository.findById(productId).orElseThrow(
                ProductNotFoundException::new);

        interestedProductRepository.deleteByUserAndProduct(user, product); //데이터 삭제
    }

    //사용자 모든 관심 제품 가져오기
    @Transactional(readOnly = true)
    public MyPageInterestedProductResponseDto getUserInterestedProducts(int page, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserException(INVALID_USER));
        PageRequest pageRequest = PageRequest.of(page, 9, Sort.by(Sort.Direction.DESC, "date"));
        Page<InterestedProduct> result = interestedProductRepository.findByUserId(userId, pageRequest);
        List<InterestedProduct> content = result.getContent();

        //사용자 관심 상품 & 성분 & 진단 종류를 가져오기
        List<MyPageInterestedProductDto> data = new ArrayList<>();
        for (InterestedProduct interestedProduct : content) {
            Product product = interestedProduct.getProduct();
            MyPageInterestedProductDto interestedProductDto = MyPageInterestedProductDto.builder()
                    .productName(product.getProductName())
                    .price(product.getPrice())
                    .productId(product.getId())
                    .element(product.getProductElements())
                    .imageLink(product.getImageLink())
                    .salesLink(product.getSalesLink())
                    .diagnoseType(product.getProductDiagnoseType())
                    .build();
            data.add(interestedProductDto);
        }
        //페이지네이션 추가하여 리턴
        return new MyPageInterestedProductResponseDto(
                result.getTotalPages(), result.getNumber(), data);
    }
}
