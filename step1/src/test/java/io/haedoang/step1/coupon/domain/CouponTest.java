package io.haedoang.step1.coupon.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("쿠폰 도메인 테스트")
class CouponTest {

    @Test
    @DisplayName("쿠폰 객체생성하기")
    void create() {
        //given
        String couponId = UUID.randomUUID().toString();
        int remained = 50;

        //when
        Coupon actual = Coupon.from(couponId, remained);

        //then
        assertAll(
                () -> assertThat(actual.getCouponId()).isNotEmpty(),
                () -> assertThat(actual.getRemained()).isEqualTo(50),
                () -> assertThat(actual.getUsed()).isEqualTo(0),
                () -> assertThat(actual.isSoldOut()).isFalse()
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {-5, -1, 0})
    @DisplayName("쿠폰 잔여 수량은 양수여야 한다")
    void failByRemained(int candidate) {
        //given
        String couponId = UUID.randomUUID().toString();

        //when & then
        assertThatThrownBy(() -> Coupon.from(couponId, candidate))
                .isInstanceOf(IllegalArgumentException.class);
    }
}