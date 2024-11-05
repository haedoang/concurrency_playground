package io.haedoang.step1.coupon.infra;

import io.haedoang.step1.coupon.domain.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    Optional<Coupon> findByCouponId(String couponId);
}
