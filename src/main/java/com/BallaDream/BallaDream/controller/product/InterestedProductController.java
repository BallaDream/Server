package com.BallaDream.BallaDream.controller.product;

import com.BallaDream.BallaDream.domain.enums.DiagnoseType;
import com.BallaDream.BallaDream.dto.message.ResponseDto;
import com.BallaDream.BallaDream.dto.product.InterestedProductRequestDto;
import com.BallaDream.BallaDream.service.product.InterestedProductService;
import com.BallaDream.BallaDream.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class InterestedProductController {

    private final InterestedProductService productService;
    private final UserService userService;

    //관심 있는 화장품을 등록
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/interested-product")
    public ResponseEntity<ResponseDto> addInterestedProduct(@RequestBody @Validated InterestedProductRequestDto interestedProductDto) {

        String username = userService.getUsernameInToken();
        productService.addInterestedProduct(interestedProductDto.getProductId(), interestedProductDto.getDiagnoseType(), username);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDto.of(HttpStatus.OK, "관심 상품을 등록하였습니다."));
    }

    //관심 있는 화장품을 해제
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/interested-product")
    public ResponseEntity<ResponseDto> deleteInterestedProduct(@RequestParam Long productId,
                                                               @RequestParam DiagnoseType diagnoseType) {

        String username = userService.getUsernameInToken();
        productService.deleteInterestedProduct(productId, diagnoseType, username);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDto.of(HttpStatus.OK, "관심 상품을 등록 해제하였습니다."));
    }

    //마이페이지에서 관심있는 제품들을 보여준다.
    //페이지네이션?
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/mypage/interested-product")
    public void getMyInterestedProduct(@RequestParam DiagnoseType diagnoseType,
                                       @RequestParam(required = false) String formulation) {


    }
}
