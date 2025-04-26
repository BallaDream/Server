package com.BallaDream.BallaDream.service.product;

import com.BallaDream.BallaDream.constants.ResponseCode;
import com.BallaDream.BallaDream.domain.enums.DiagnosisType;
import com.BallaDream.BallaDream.domain.user.User;
import com.BallaDream.BallaDream.dto.RecommendProductQueryDto;
import com.BallaDream.BallaDream.dto.product.RecommendProductDto;
import com.BallaDream.BallaDream.dto.product.RecommendationProductResponseDto;
import com.BallaDream.BallaDream.exception.user.UserException;
import com.BallaDream.BallaDream.repository.product.ProductRepository;
import com.BallaDream.BallaDream.repository.product.query.ProductQueryRepository;
import com.BallaDream.BallaDream.repository.user.UserRepository;
import com.BallaDream.BallaDream.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductQueryRepository productQueryRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public RecommendationProductResponseDto createRecommend(DiagnosisType diagnosisType,
                                                            String formulation, Integer minPrice, Integer maxPrice,
                                                            int step) {
        String username = userService.getUsernameInToken();
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new UserException(ResponseCode.INVALID_USER));

        List<RecommendProductQueryDto> queryDto = productQueryRepository.recommendProduct(user.getId(), diagnosisType,
                formulation, minPrice, maxPrice, step * 8, 8);

        List<RecommendProductDto> result = mapToRecommendProductDto(queryDto);
        for (RecommendProductDto recommendProductDto : result) {
            log.info("{} {}", recommendProductDto.getProductName(), recommendProductDto.getElement().size());
        }

        return new RecommendationProductResponseDto("ha", result);
    }

    private List<RecommendProductDto> mapToRecommendProductDto(List<RecommendProductQueryDto> queryDto) {
        return queryDto.stream()
                //element 을 제외한 나머지 데이터를 기준으로 그룹핑 작업을 수행한다.
                .collect(Collectors.groupingBy(
                        dto -> new ProductKey(
                                dto.getProductId(),
                                dto.getProductName(),
                                dto.getFormulation(),
                                dto.getPrice(),
                                dto.getSalesLink(),
                                dto.getImageLink(),
                                dto.isInterested()
                        )
                ))
                .entrySet().stream()
                .map(entry -> {
                    ProductKey key = entry.getKey();
                    List<String> elements = entry.getValue().stream()
                            .map(RecommendProductQueryDto::getElementName)
                            .distinct() // 중복 제거
                            .sorted()   // 정렬 (필요 없으면 빼도 됨)
                            .toList();

                    return new RecommendProductDto(
                            key.productId(),
                            key.productName(),
                            key.formulation(),
                            String.valueOf(key.price()),
                            key.salesLink(),
                            key.imageLink(),
                            key.isInterested(),
                            elements
                    );
                })
                .toList();
    }

    //데이터 담는 전용 객체
    private record ProductKey(
            Long productId,
            String productName,
            String formulation,
            int price,
            String salesLink,
            String imageLink,
            boolean isInterested
    ) {}
}
