package com.BallaDream.BallaDream.service.product;

import com.BallaDream.BallaDream.domain.enums.DiagnoseType;
import com.BallaDream.BallaDream.domain.enums.LoginType;
import com.BallaDream.BallaDream.domain.enums.UserRole;
import com.BallaDream.BallaDream.domain.product.InterestedProduct;
import com.BallaDream.BallaDream.domain.user.User;
import com.BallaDream.BallaDream.exception.user.InvalidInputException;
import com.BallaDream.BallaDream.repository.product.InterestedProductRepository;
import com.BallaDream.BallaDream.repository.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
class InterestedProductServiceTest {

    @Autowired InterestedProductService productService;
    @Autowired InterestedProductRepository interestedProductRepository;
    @Autowired UserRepository userRepository;

    @Test
    @Transactional
    @DisplayName("하나의 관심 상품은 여러 사용자가 등록할 수 있다.")
    void addInterestedProduct() {

        //given
        User user1 = new User("test1", "pass", LoginType.WEB, UserRole.ROLE_USER);
        User user2 = new User("test2", "pass", LoginType.WEB, UserRole.ROLE_USER);
        userRepository.save(user1);
        userRepository.save(user2);
        //when
        productService.addInterestedProduct(2L, user1.getId());
        productService.addInterestedProduct(2L,user2.getId());

        //then
        List<InterestedProduct> result = interestedProductRepository.findAll();
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    @Transactional
    @DisplayName("유효하지 않은 데이터를 등록 시도")
    void addWrongData() {

        //given
        User user1 = new User("test1", "pass", LoginType.WEB, UserRole.ROLE_USER);

        //when
        userRepository.save(user1);

        //then
        assertThatThrownBy(
                () -> productService.addInterestedProduct(1L, user1.getId()))
                .isInstanceOf(InvalidInputException.class);
    }
}