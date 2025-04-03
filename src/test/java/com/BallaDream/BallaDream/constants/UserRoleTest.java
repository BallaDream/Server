package com.BallaDream.BallaDream.constants;

import com.BallaDream.BallaDream.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class UserRoleTest {

    @Test
    void userRoleEqualTest() {

        //given
        User user = new User("test", UserRole.ROLE_USER);
        //when
        UserRole roleUser = UserRole.valueOfRole("ROLE_USER");
        //then
        Assertions.assertThat(roleUser).isNotNull();
    }
}