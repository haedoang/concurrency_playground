package io.haedoang.step1.coupon.repository;

import io.haedoang.step1.coupon.domain.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
}
