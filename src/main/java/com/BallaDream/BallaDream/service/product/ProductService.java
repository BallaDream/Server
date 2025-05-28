package com.BallaDream.BallaDream.service.product;

import com.BallaDream.BallaDream.constants.ResponseCode;
import com.BallaDream.BallaDream.domain.enums.DiagnoseType;
import com.BallaDream.BallaDream.domain.enums.Level;
import com.BallaDream.BallaDream.domain.user.User;
import com.BallaDream.BallaDream.dto.product.RecommendProductQueryDto;
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

import java.util.LinkedHashMap;
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

    //피부 진단 직후, 화장품을 추천
    public RecommendationProductResponseDto createRecommend(Long userId, DiagnoseType diagnoseType, Level level,
                                                            String formulation, Integer minPrice, Integer maxPrice,
                                                            int step) {
//        String username = userService.getUsernameInToken();
//        Long userId = userService.getUserId();
//        User user = userRepository.findByUsername(username).orElseThrow(() ->
//                new UserException(ResponseCode.INVALID_USER));

        //Todo 몇개씩 데이터를 전달할 것인지 정할것
        List<RecommendProductQueryDto> queryDto = productQueryRepository.recommendProduct(userId, diagnoseType, level,
                formulation, minPrice, maxPrice, step * 4, 4);

        List<RecommendProductDto> result = mapToRecommendProductDto(queryDto);
        return new RecommendationProductResponseDto(result);
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
                        ),
                        LinkedHashMap::new,  //순서 유지되는 Map 사용
                        Collectors.toList()
                ))
                .entrySet().stream()
                .map(entry -> {
                    ProductKey key = entry.getKey();
                    List<String> elements = entry.getValue().stream()
                            .map(RecommendProductQueryDto::getElementName)
                            .distinct() // 중복 제거
                            .sorted()   // 정렬
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
