package io.haedoang.step1.coupon.repository;

import io.haedoang.step1.coupon.domain.Coupon;
import io.haedoang.step1.coupon.infra.CouponRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CouponRepositoryTest {

    @Autowired
    CouponRepository couponRepository;

    @Test
    @DisplayName("쿠폰 생성하기")
    void create() {
        //given
        Coupon coupon = Coupon.from(UUID.randomUUID().toString(), 50);

        //when
        couponRepository.save(coupon);

        //then
        assertThat(coupon.getKey()).isNotNull();
    }
}